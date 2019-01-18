package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;

import java.util.List;

public interface OrderService {
    void insertOrder(Order order);

    List<Order> selectAllOrder();
}
