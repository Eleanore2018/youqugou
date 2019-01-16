package cn.itcast.core.service;

import cn.itcast.common.util.IdWorker;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.*;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojogroup.OrderItemVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private PayLogDao payLogDao;

    @Autowired
    private SellerDao sellerDao;   //张静
    @Autowired
    private GoodsDao goodsDao; //张静

    @Autowired
    private ItemCatDao itemCatDao;

    @Override
    public void insertOrder(Order order) {
        List<Cart> carts = (List<Cart>) redisTemplate.boundHashOps("cart").get(order.getUserId());
        double totalPrice = 0;
        List<Long> orderIds = new ArrayList<>();
        for (Cart cart : carts) {
            // 生成订单id
            long orderId = idWorker.nextId();
            order.setOrderId(orderId);
            orderIds.add(orderId);
            // 设置订单状态 0 未支付 1 已支付 2...
            order.setStatus("0");
            // 创建时间
            order.setCreateTime(new Date());
            // 更新时间
            order.setUpdateTime(new Date());
            // 商家id
            order.setSellerId(cart.getSellerId());
            // 订单总金额
            double payment = 0;
            for (OrderItem orderItem : cart.getOrderItems()) {
                Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                // 生成订单项id
                long orderItemId = idWorker.nextId();
                orderItem.setId(orderItemId);
                // 设置订单id作为外键
                orderItem.setOrderId(orderId);
                // 设置商品id
                orderItem.setGoodsId(item.getGoodsId());
                // 设置订单标题
                orderItem.setTitle(item.getTitle());
                // 设置价格
                orderItem.setPrice(item.getPrice());
                // 设置图片
                orderItem.setPicPath(item.getImage());
                // 设置商家id
                orderItem.setSellerId(item.getSellerId());
                //设置状态  张静
                orderItem.setStatus("0");
                // 小计
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getNum()));
                // 保存订单项数据
                orderItemDao.insertSelective(orderItem);

                payment += orderItem.getTotalFee().doubleValue();
            }
            // 设置订单总金额
            order.setPayment(new BigDecimal(payment));

            totalPrice += payment;
            // 保存订单
            orderDao.insertSelective(order);
        }

        // 生成日志
        PayLog payLog = new PayLog();
        // 生成支付订单id
        long outTradeNo = idWorker.nextId();
        payLog.setOutTradeNo(String.valueOf(outTradeNo));
        // 支付订单生成时间
        payLog.setCreateTime(new Date());
        // 设置支付订单总金额 单位(分)
        payLog.setTotalFee((long) (totalPrice * 100));
        // 设置支付用户
        payLog.setUserId(order.getUserId());
        // 设置支付状态
        payLog.setTradeState("0");
        // 设置订单id集合(字符串)
        payLog.setOrderList(orderIds.toString().replace("[", "").replace("]", ""));
        // 支付类型
        payLog.setPayType("1");
        // 保存支付订单
        payLogDao.insertSelective(payLog);
        redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);
    }

    //查询所有订单功能,分页显示
    //张静  2018-12-28
    @Override
    public PageResult search(String status, String name, Integer pageNo, Integer pageSize) {
        //查询订单
        OrderQuery query = new OrderQuery();
        OrderQuery.Criteria criteria = query.createCriteria();
        if (!"".equals(status)) {
            criteria.andStatusEqualTo(status);
        }
        criteria.andUserIdEqualTo(name);

        PageHelper.startPage(pageNo, pageSize);
        //查询当前用户的订单
        List<Order> orderList = orderDao.selectByExample(query);
        if (orderList != null && orderList.size() > 0) {
            for (Order order : orderList) {
                order.setOrderIdStr(String.valueOf(order.getOrderId()));
                //查询卖家店铺
                Seller seller = sellerDao.selectByPrimaryKey(order.getSellerId());
                order.setBuyerNick(seller.getNickName());
                //查询当前用户订单详情
                OrderItemQuery itemQuery = new OrderItemQuery();
                itemQuery.createCriteria().andOrderIdEqualTo(order.getOrderId());
                List<OrderItem> orderItems = orderItemDao.selectByExample(itemQuery);
                if (orderItems != null && orderItems.size() > 0) {
                    for (OrderItem orderItem : orderItems) {
                        //获取商品名称
                        Goods goods = goodsDao.selectByPrimaryKey(orderItem.getGoodsId());
                        orderItem.setSpecStr(orderItem.getTitle().replace(goods.getGoodsName(), ""));
                        orderItem.setGoodsName(goods.getGoodsName());
                        orderItem.setIdStr(String.valueOf(orderItem.getId()));
                    }
                }
                order.setOrderItemList(orderItems);
            }
        }
        //查询订单数
        Page<Order> p = (Page<Order>) orderList;
        long total = p.getTotal();
        List<Order> result = p.getResult();
        return new PageResult(total, result);
    }


    //查询指定订单  张静  2018-12-29
    @Override
    public OrderItemVo selectOrderItemById(Long id) {
        OrderItemVo vo = new OrderItemVo();
        OrderItem orderItem = orderItemDao.selectByPrimaryKey(id);
        orderItem.setIdStr(String.valueOf(orderItem.getId()));
        //获取商品名称
        Goods goods = goodsDao.selectByPrimaryKey(orderItem.getGoodsId());
        orderItem.setSpecStr(orderItem.getTitle().replace(goods.getGoodsName(), ""));
        orderItem.setGoodsName(goods.getGoodsName());
        vo.setOrderItem(orderItem);
        Order order = orderDao.selectByPrimaryKey(orderItem.getOrderId());
        vo.setAddr(order.getReceiverAreaName());
        vo.setCongignTime(order.getConsignTime());
        vo.setCreateTime(order.getCreateTime());
        vo.setPaymentTime(order.getPaymentTime());
        vo.setOrderIdStr(String.valueOf(orderItem.getOrderId()));
        vo.setPaymentType(order.getPaymentType() == "0" ? "微信" : "货到付款");
        return vo;
    }


    //支付指定的订单 张静 12月29日
    @Override
    public void toPay(Long id) {
        Order order = orderDao.selectByPrimaryKey(id);
        // 生成日志
        PayLog payLog = new PayLog();
        // 生成支付订单id
        long outTradeNo = idWorker.nextId();
        payLog.setOutTradeNo(String.valueOf(outTradeNo));
        // 支付订单生成时间
        payLog.setCreateTime(new Date());
        // 设置支付订单总金额 单位(分)
        double totalPrice = order.getPayment().doubleValue();
        payLog.setTotalFee((long) (totalPrice * 100));
        // 设置支付用户
        payLog.setUserId(order.getUserId());
        // 设置支付状态
        payLog.setTradeState("0");
        // 设置订单id集合(字符串)
        List<Long> list = new ArrayList<>();
        list.add(order.getOrderId());
        payLog.setOrderList(list.toString().replace("[", "").replace("]", ""));
        // 支付类型
        payLog.setPayType("1");
        // 保存支付订单
        payLogDao.insertSelective(payLog);
        redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);
    }

    /*查询所有订单 张静 2018-12-30*/
    @Override
    public PageResult searchOrder(Integer pageNum, Integer pageSize, Order order) {
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("order_id desc");
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        if (null != order.getOrderIdStr() && !"".equals(order.getOrderIdStr().trim())) {
            criteria.andOrderIdEqualTo(Long.parseLong(order.getOrderIdStr().trim()));
        }
        if (null != order.getStatus() && !"".equals(order.getStatus().trim())) {
            criteria.andStatusEqualTo(order.getStatus());
        }
        //如果sellerid不为空,表示查询商户自己的数据,否则是运营商查询,查询所有商品数据  张静 2018-12-30
        if (order.getSellerId() != null) {
            criteria.andSellerIdEqualTo(order.getSellerId());
        }
        Page<Order> page = (Page<Order>) orderDao.selectByExample(orderQuery);
        List<Order> result = page.getResult();
        if (null != result && result.size() > 0) {
            for (Order o : result) {
                o.setOrderIdStr(o.getOrderId().toString());
            }
        }
        return new PageResult(page.getTotal(), page.getResult());
    }


    //查询指定订单 张静 2018-12-30
    @Override
    public List<OrderItem> selectOrderById(Long id) {
        OrderItemQuery query = new OrderItemQuery();
        query.createCriteria().andOrderIdEqualTo(id);
        List<OrderItem> orderItems = orderItemDao.selectByExample(query);
        if (null != orderItems && orderItems.size() > 0) {
            for (OrderItem orderItem : orderItems) {
                orderItem.setIdStr(orderItem.getId().toString());
            }
        }
        return orderItems;
    }


    //更新指定id的订单(发货) 张静 2018-12-30
    @Override
    public void updateOrderById(Order order) {
        order.setStatus("4");
        orderDao.updateByPrimaryKeySelective(order);
    }

    /**
     * Author: Mr Liu
     * Date: 2019/1/2 08:25
     *
     * @param sellerId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Map<String, List> getSalesByCategory(String sellerId, Date startDate, Date endDate) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Map<String, List> pieCharts = new HashMap<>();
            long nowTime = format1.parse(format1.format(new Date())).getTime();
            if (endDate.getTime() < nowTime) {
                if (redisTemplate.boundHashOps(sellerId).get("orderStats") == null) {
                    pieCharts = getOrderStats(sellerId,startDate,endDate);
                    redisTemplate.boundHashOps(sellerId).put("orderStats", pieCharts);
                } else {
                    pieCharts = (Map<String, List>) redisTemplate.boundHashOps(sellerId).get("orderStats");
                }
                return pieCharts;
            } else if (endDate.getTime() >= nowTime && startDate.getTime() <= nowTime) {
                pieCharts = getOrderStats(sellerId,startDate,endDate);
                return pieCharts;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, List> getOrderStats(String sellerId, Date startDate, Date endDate) {
        Map<String, List> pieChart = new HashMap<>();
        List paymentsList = new ArrayList();
        List itemNameList = new ArrayList();
        OrderStat orderStat = new OrderStat();
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        criteria.andSellerIdEqualTo(sellerId).andStatusEqualTo("1").andUpdateTimeBetween(startDate, endDate);
        List<Order> orders = orderDao.selectByExample(orderQuery);
        List<OrderStat> orderStats = new ArrayList<>();
        for (Order order : orders) {
            OrderItemQuery orderItemQuery = new OrderItemQuery();
            orderItemQuery.createCriteria().andOrderIdEqualTo(order.getOrderId()).andSellerIdEqualTo(sellerId);
            List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);
            for (OrderItem orderItem : orderItems) {
                orderStat.setPayment(orderItem.getTotalFee().doubleValue());
                orderStat.setGoodsId(orderItem.getGoodsId());
                orderStats.add(orderStat);
            }
        }
        Set<Long> category1Ids = new HashSet<>();
        for (OrderStat stat : orderStats) {
            Long category1Id = goodsDao.selectcategory1IdByPrimaryKey(stat.getGoodsId());
            stat.setCategory1Id(category1Id);
            category1Ids.add(category1Id);
        }
        for (Long category1Id : category1Ids) {
            ItemCat itemCat = itemCatDao.selectByPrimaryKey(category1Id);
            Double payments = 0.0;
            for (OrderStat stat : orderStats) {
                if (stat.getCategory1Id() == category1Id) {
                    payments += stat.getPayment();
                }
            }
            paymentsList.add(payments);
            itemNameList.add(itemCat.getName());
        }
        pieChart.put("itemNameList", itemNameList);
        pieChart.put("paymentsList", paymentsList);
        return pieChart;
    }
}
