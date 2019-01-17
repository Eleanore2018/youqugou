package cn.itcast.core.controller;


import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*张静 2019-01-01
* 秒杀订单管理
* */
@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {


    @Reference
    private SeckillOrderService seckillOrderService;


    /*实现秒杀订单查询功能 张静 2019-01-01*/
    @RequestMapping("/search")
    public PageResult search(Integer pageNum, Integer pageSize, @RequestBody SeckillOrder seckillOrder){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        seckillOrder.setSellerId(name);
        return seckillOrderService.search(pageNum,pageSize,seckillOrder);
    }

    /*查询指定订单 张静 2019-01-01*/
    @RequestMapping("/selectOrderById")
    public SeckillOrder selectOrderById(Long id){
        return seckillOrderService.selectOrderById(id);
    }

}
