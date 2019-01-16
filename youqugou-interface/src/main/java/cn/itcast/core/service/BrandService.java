package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    List<Map> selectAllBrandMap();

    PageResult selectPageBrand(Integer pageNum, Integer pageSize);

    int insertBrand(Brand brand);

    Brand selectBrandById(Long id);

    int updateBrand(Brand brand);

    int deleteBrandByIds(Long[] ids);

    PageResult searchBrand(Integer pageNum, Integer pageSize, Brand brand);

    /*贾运通*/
    void updateStatus(Long[] ids, String status);

}
