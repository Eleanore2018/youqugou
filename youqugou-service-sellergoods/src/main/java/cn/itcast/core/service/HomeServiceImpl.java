package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author:Mr Liu
 * Date:2018-12-28
 */
@Service
@Transactional
public class HomeServiceImpl implements HomeService {
    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询商家审核通过时的日期
     *
     * @return
     */
    @Override
    public Date searchTime(String sellerId) {
        return sellerDao.selectByPrimaryKey(sellerId).getCreateTime();
    }

    /**
     * 查询商家销量
     *
     * @param list
     * @param sellerId
     * @return
     */
    @Override
    public List<Double> getSales(List<Date> list, String sellerId) {
        List<Double> verticalAxis = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long nowTime = format1.parse(format1.format(new Date())).getTime();
            for (Date date : list) {

                // 如果需要查询的数据在今天之前
                if (date.getTime() < nowTime) {
                    // 继续判断缓存中是否已经存储 使用商家id作为大键,日期作为小键
                    if (redisTemplate.boundHashOps(sellerId).get(date) == null) {
                        OrderQuery orderQuery = new OrderQuery();
                        orderQuery.createCriteria().andCreateTimeBetween(date, format.parse(format1.format(date) + " 23:59:59")).andSellerIdEqualTo(sellerId).andStatusEqualTo("1");

                        // 查询订单id集合
                        List<BigDecimal> payments = orderDao.selectPaymentByExample(orderQuery);
                        // 如果集合为空或者size为0,则说明今日销量为0
                        if (payments != null && payments.size() > 0) {
                            double num = 0.0;
                            for (BigDecimal payment : payments) {
                                num += payment.doubleValue();
                            }
                            verticalAxis.add(num);
                            redisTemplate.boundHashOps(sellerId).put(date, num);
                        } else {
                            verticalAxis.add(0.0);
                            redisTemplate.boundHashOps(sellerId).put(date, 0.0);
                        }
                    } else {
                        verticalAxis.add((double) redisTemplate.boundHashOps(sellerId).get(date));
                    }
                } else if (date.getTime() == nowTime) {
                    OrderQuery orderQuery = new OrderQuery();
                    orderQuery.createCriteria().andCreateTimeBetween(date, format.parse(format1.format(date) + " 23:59:59")).andSellerIdEqualTo(sellerId).andStatusEqualTo("1");

                    // 查询订单id集合
                    List<BigDecimal> payments = orderDao.selectPaymentByExample(orderQuery);
                    // 如果集合为空或者size为0,则说明今日销量为0
                    if (payments != null && payments.size() > 0) {
                        double num = 0.0;
                        for (BigDecimal payment : payments) {
                            num += payment.doubleValue();
                        }
                        verticalAxis.add(num);
                    } else {
                        verticalAxis.add(0.0);
                    }
                } else {
                    verticalAxis.add(0.0);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return verticalAxis;
    }

    /**
     * Author Mr Liu
     * Date 2019-1-1
     * the result use to generate chart
     * @param list
     * @return
     */
    @Override
    public List<Double> getSales(List<Date> list) {
        List<Double> verticalAxis = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long nowTime = format1.parse(format1.format(new Date())).getTime();
            for (Date date : list) {
                // 如果需要查询的数据在今天之前
                if (date.getTime() < nowTime) {
                    // 继续判断缓存中是否已经存储 使用商家id作为大键,日期作为小键
                    if (redisTemplate.boundHashOps("admin").get(date) == null) {
                        OrderQuery orderQuery = new OrderQuery();
                        orderQuery.createCriteria().andCreateTimeBetween(date, format.parse(format1.format(date) + " 23:59:59")).andStatusEqualTo("1");
                        // 查询订单id集合
                        List<BigDecimal> payments = orderDao.selectPaymentByExample(orderQuery);

                        // 如果集合为空或者size为0,则说明今日销量为0
                        if (payments != null && payments.size() > 0) {
                            double num = 0.0;
                            for (BigDecimal payment : payments) {
                                num += payment.doubleValue();
                            }
                            verticalAxis.add(num);
                            redisTemplate.boundHashOps("admin").put(date, num);
                        } else {
                            verticalAxis.add(0.0);
                            redisTemplate.boundHashOps("admin").put(date, 0.0);
                        }
                    } else {
                        verticalAxis.add((double) redisTemplate.boundHashOps("admin").get(date));
                    }
                } else if (date.getTime() == nowTime) {
                    OrderQuery orderQuery = new OrderQuery();
                    orderQuery.createCriteria().andCreateTimeBetween(date, format.parse(format1.format(date) + " 23:59:59")).andStatusEqualTo("1");
                    // 查询订单id集合
                    List<BigDecimal> payments = orderDao.selectPaymentByExample(orderQuery);

                    // 如果集合为空或者size为0,则说明今日销量为0
                    if (payments != null && payments.size() > 0) {
                        double num = 0.0;
                        for (BigDecimal payment : payments) {
                            num += payment.doubleValue();
                        }
                        verticalAxis.add(num);
                    } else {
                        verticalAxis.add(0.0);
                    }
                } else {
                    verticalAxis.add(0.0);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return verticalAxis;
    }
}