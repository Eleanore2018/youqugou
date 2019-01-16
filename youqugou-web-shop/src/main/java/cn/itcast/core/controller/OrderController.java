package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojogroup.OrderItemVo;
import cn.itcast.core.service.OrderService;
import cn.itcast.core.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
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
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(name);
        return orderService.searchOrder(pageNum, pageSize, order);
    }

    /*查询所有订单 张静 2018-12-30*/
    @RequestMapping("/selectOrderById")
    public List<OrderItem> selectOrderById(Long id) {
        return orderService.selectOrderById(id);
    }

    //查询指定id的订单详情 张静  2018-12-30
    @RequestMapping("/selectOrderItemById")
    public OrderItemVo selectOrderItemById(Long id) {
        return orderService.selectOrderItemById(id);
    }

    //更新指定id的订单(发货) 张静 2018-12-30
    @RequestMapping("/updateOrderById")
    public Result updateOrderById(@RequestBody Order order) {
        try {
            orderService.updateOrderById(order);
            return new Result(true, "发货成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发货失败!");
        }
    }

    /**
     * Author: Mr Liu
     * Date: 2019/1/2 00:26
     * use to get category and sales by sellerId
     */
    @RequestMapping("/getSalesByCategory")
    public List<Map<String, Object>> getSalesByCategory(Integer year, Integer month, String timeBucket) throws ParseException {
        // 获取当前登录商家
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 按照-规则切割时间段,获取到一头一尾的数据
        String[] split = timeBucket.split("-");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 开始时间
        Date startDate = format.parse("" + year + "-" + month + "-" + split[0]);
        // 结束时间
        Date endDate = format.parse("" + year + "-" + month + "-" + split[1]);
        return orderService.getSalesByCategory(sellerId, startDate, endDate);
    }
}
