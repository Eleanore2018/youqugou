package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojogroup.OrderItemVo;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /**
     * Author: Mr Liu
     * Date: 2019/1/8 20:26
     * use to get category and sales
     */
    @RequestMapping("/getSalesByCategory2Operator")
    public List<Map<String, Object>> getSalesByCategory2Operator(Integer year, Integer month, String timeBucket) throws ParseException {
        // 按照-规则切割时间段,获取到一头一尾的数据
        String[] split = timeBucket.split("-");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 开始时间
        Date startDate = format.parse("" + year + "-" + month + "-" + split[0]);
        // 结束时间
        Date endDate = format.parse("" + year + "-" + month + "-" + split[1]);
        return orderService.getSalesByCategory2Operator(startDate, endDate);
    }
}
