package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojogroup.OrderItemVo;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @RequestMapping("/add")
    public Result submitOrder(@RequestBody Order order) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            order.setUserId(username);
            orderService.insertOrder(order);
            return new Result(true, "提交订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "提交订单失败");
        }
    }

    //订单查询功能,分页  张静 2018-12-29
    @RequestMapping("/search")
    public PageResult search(String status,Integer pageNo, Integer pageSize){
        //获取当前登录人
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderService.search(status,name,pageNo,pageSize);
    }


    //查询指定id的订单详情 张静  2018-12-29
    @RequestMapping("/selectOrderItemById")
    public OrderItemVo selectOrderItemById(Long id){
        return orderService.selectOrderItemById(id);
    }

    //支付指定的订单 张静   2018-12-29
    @RequestMapping("/toPay")
    public Result toPay(Long id){
        try {
            orderService.toPay(id);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }
}
