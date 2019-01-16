package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;

import java.util.List;

public interface SeckillGoodsService {


    //张静 2018-12-31 查询秒杀商品
    List<SeckillGoods> findSeckillList();


    //张静 查询指定的商品  2018-12-31
    SeckillGoods findOne(Long id);


    //实现商品秒杀 张静 2019-01-01
    boolean goToSeckill(String name, Long id);
}
