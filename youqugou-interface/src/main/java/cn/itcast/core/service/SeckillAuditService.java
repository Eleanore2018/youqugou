package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;

public interface SeckillAuditService {
    PageResult searchSeckillAudit(Integer pageNum, Integer pageSize, SeckillGoods seckillGoods);

    void updateStatus(Long[] ids, String status);
}
