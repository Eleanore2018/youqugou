package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojogroup.SeckillOrderVo;

import java.util.List;

public interface SeckillOrderService {

    /*实现秒杀订单查询功能 张静 2019-01-01*/
    PageResult search(Integer pageNum, Integer pageSize, SeckillOrder seckillOrder);


    /*查询指定订单 张静 2019-01-01*/
    SeckillOrder selectOrderById(Long id);

    /*分页查询订单 张静 2019-01-01*/
    PageResult searchSeckillOrderVoList(String name, Integer pageNum, Integer pageSize);

    //根据id取消订单 张静 2019-01-01
    void cancelOrderById(Long id);

    //根据id查询订单的包装对象 张静 2019-01-01
    SeckillOrderVo selectOrderVoById(Long id);
}
