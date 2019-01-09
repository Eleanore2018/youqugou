package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojogroup.GoodsVo;

public interface GoodsService {
    void insertGoodsVo(GoodsVo goodsVo);

    PageResult searchGoodsPage(Integer pageNum, Integer pageSize, Goods goods);

    GoodsVo selectGoodsVoById(Long id);

    void updateGoodsVo(GoodsVo goodsVo);

    void updateStatus(Long[] ids, String status);

    void delete(Long[] ids);
}
