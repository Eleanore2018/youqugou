package cn.itcast.core.service;

import cn.itcast.common.util.IdWorker;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        payLog.setTotalFee((long)(totalPrice*100));
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
}
