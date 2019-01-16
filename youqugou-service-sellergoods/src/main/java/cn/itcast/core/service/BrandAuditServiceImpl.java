package cn.itcast.core.service;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 贾运通2018/12/30
 */

@Service
@Transactional
public class BrandAuditServiceImpl implements BrandAuditService{

    @Autowired
    private BrandDao brandDao;

    //品牌审核页数据查询
    @Override
    public PageResult searchBrandAudit(Integer pageNum, Integer pageSize,Brand brand) {
        PageHelper.startPage(pageNum, pageSize);

        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        criteria.andStatusEqualTo("0");//只查询未审核的

        if (null != brand.getName() && !"".equals(brand.getName().trim())){
            criteria.andNameLike("%" + brand.getName() + "%");
        }
        if (null != brand.getFirstChar() && !"".equals(brand.getFirstChar().trim())) {
            criteria.andFirstCharEqualTo(brand.getFirstChar());
        }

        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }


    //品牌审核
    @Override
    public void updateStatus(Long[] ids, String status) {
        Brand brand = new Brand();
        brand.setStatus(status);
        for (Long id : ids) {
            brand.setId(id);
            brandDao.updateByPrimaryKeySelective(brand);
        }
    }
}
