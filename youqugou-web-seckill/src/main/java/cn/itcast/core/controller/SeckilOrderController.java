package cn.itcast.core.controller;

import cn.itcast.core.pojogroup.SeckillOrderVo;
import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckilOrder")
public class SeckilOrderController {

    @Reference
    private SeckillOrderService seckillOrderService;

    //根据id查询指定的订单
    @RequestMapping("/findOrderById")
    public SeckillOrderVo findOrderVoById(Long id){
        return seckillOrderService.selectOrderVoById(id);
    }
}
