package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojogroup.OrderItemVo;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单管理 张静 2018-12-30
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;


    /*查询所有订单 2018-12-30 张静*/
    @RequestMapping("/searchOrder")
    public PageResult searchOrder(Integer pageNum, Integer pageSize, @RequestBody Order order) {
       return orderService.searchOrder(pageNum,pageSize,order);
    }


    /*查询所有订单 张静 2018-12-30*/
    @RequestMapping("/selectOrderById")
    public List<OrderItem> selectOrderById(Long id){
        return orderService.selectOrderById(id);
    }

    //查询指定id的订单详情 张静  2018-12-30
    @RequestMapping("/selectOrderItemById")
    public OrderItemVo selectOrderItemById(Long id){
        return orderService.selectOrderItemById(id);
    }

}
