package cn.itcast.core.controller;


import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.javafx.logging.PulseLogger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {

    @Reference
    private SeckillOrderService seckillOrderService;

    //分页查询秒杀订单
    @RequestMapping("/search")
    public PageResult search(Integer pageNum,Integer pageSize){
        //获取当前登录人
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return seckillOrderService.searchSeckillOrderVoList(name,pageNum,pageSize);
    }


    //根据id取消订单 张静 2019-01-01
    @RequestMapping("/cancelOrderById")
    public Result cancelOrderById(Long id){
        try {
            seckillOrderService.cancelOrderById(id);
            return new Result(true,"成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败!");
        }
    }
}
