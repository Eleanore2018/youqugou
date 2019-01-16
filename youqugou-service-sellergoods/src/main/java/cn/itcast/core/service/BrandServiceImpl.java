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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;

    @Override
    public List<Map> selectAllBrandMap() {
        return brandDao.selectAllBrandMap();
    }

    @Override
    public PageResult selectPageBrand(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Brand> brandPage = (Page<Brand>) brandDao.selectByExample(null);
        return new PageResult(brandPage.getTotal(),brandPage.getResult());
    }

    @Override
    public int insertBrand(Brand brand) {
        return brandDao.insertSelective(brand);
    }

    @Override
    public Brand selectBrandById(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateBrand(Brand brand) {
        return brandDao.updateByPrimaryKeySelective(brand);
    }

    @Override
    public int deleteBrandByIds(Long[] ids) {
        /*int row = 0;
        for (Long id : ids) {
            row = brandDao.deleteByPrimaryKey(id);
        }*/
        BrandQuery brandQuery = new BrandQuery();
        brandQuery.createCriteria().andIdIn(Arrays.asList(ids));
        return brandDao.deleteByExample(brandQuery);
    }

    @Override
    public PageResult searchBrand(Integer pageNum, Integer pageSize, Brand brand) {
        PageHelper.startPage(pageNum, pageSize);

        BrandQuery brandQuery = new BrandQuery();
        brandQuery.setOrderByClause("status");
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        if (null != brand.getName() && !"".equals(brand.getName().trim())){
            criteria.andNameLike("%" + brand.getName() + "%");
        }
        if (null != brand.getFirstChar() && !"".equals(brand.getFirstChar().trim())) {
            criteria.andFirstCharEqualTo(brand.getFirstChar());
        }

        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }



    /*贾运通2018/12/28*/
    //品牌审核[状态修改]
    @Override
    public void updateStatus(Long[] ids, String status) {
        Brand brand = new Brand();
        brand.setStatus(status);
        for (Long id : ids) {
                brand.setId(id);
                brandDao.updateByPrimaryKeySelective(brand);
        }
    }

   /* @Override
    public PageResult selectBrandAudit(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        BrandQuery brandQuery = new BrandQuery();
        brandQuery.setOrderByClause("order by status");
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }*/
}
