package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;

import cn.itcast.core.pojo.seckill.SeckillGoods;


public interface SeckillGoodsService {
    PageResult searchSeckillGoods(Integer pageNum, Integer pageSize, SeckillGoods seckillGoods, String name);

}
