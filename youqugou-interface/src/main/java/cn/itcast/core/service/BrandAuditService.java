package cn.itcast.core.service;


import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;

/**
 * 贾运通2018/12/30
 */
public interface BrandAuditService {
    PageResult searchBrandAudit(Integer pageNum, Integer pageSize, Brand brand);

    void updateStatus(Long[] ids, String status);
}
