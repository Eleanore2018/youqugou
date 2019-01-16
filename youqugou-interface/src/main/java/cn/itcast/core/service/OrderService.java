package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojogroup.OrderItemVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {
    void insertOrder(Order order);
    //订单查询功能,分页  张静 2018-12-29
    PageResult search(String status, String name, Integer pageNo, Integer pageSize);

    //查询指定id的订单详情 张静  2018-12-29
    OrderItemVo selectOrderItemById(Long id);

    //支付指定的订单 张静   2018-12-29
    void toPay(Long id);

    //查询订单 张静 2018-12-30
    PageResult searchOrder(Integer pageNum, Integer pageSize, Order order);

    //查询指定订单 张静 2018-12-30
    List<OrderItem> selectOrderById(Long id);


    //更新指定id的订单(发货) 张静 2018-12-30
    void updateOrderById(Order order);

    Map<String,List> getSalesByCategory(String sellerId, Date startDate, Date endDate);
}
