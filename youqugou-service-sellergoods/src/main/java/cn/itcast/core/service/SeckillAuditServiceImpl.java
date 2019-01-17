package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.ad.ContentQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 贾运通 2018/12/31
 */

@Service
@Transactional
public class SeckillAuditServiceImpl implements SeckillAuditService {

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Override
    public PageResult searchSeckillAudit(Integer pageNum, Integer pageSize, SeckillGoods seckillGoods) {
        // 分页开始
        PageHelper.startPage(pageNum, pageSize);
        // 设定执行查找条件
        SeckillGoodsQuery goodsQuery = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        criteria.andStatusEqualTo("0");//只查询未审核的

        if (null != seckillGoods.getTitle() && !"".equals(seckillGoods.getTitle())){
            if (seckillGoods.getTitle().contains(" ")){
                seckillGoods.setTitle(seckillGoods.getTitle().replace(" ",""));
            }
            criteria.andTitleLike("%" + seckillGoods.getTitle().trim() + "%");
        }
        if (null != seckillGoods.getSellerId() && !"".equals(seckillGoods.getSellerId())){
            criteria.andSellerIdEqualTo(seckillGoods.getSellerId().trim().replace(" ",""));
        }

        // 根据设置的条件执行查找
        Page<SeckillGoods> page = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(goodsQuery);
        // 返回需要的分页数据
        return new PageResult(page.getTotal(), page.getResult());
    }



    @Override
    public void updateStatus(Long[] ids, String status) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setStatus(status);
        for (Long id : ids) {
            seckillGoods.setId(id);
            seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
        }
    }
}
