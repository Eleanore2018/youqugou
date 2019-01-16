package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;

public interface SpecificationAuditService {
    PageResult searchSpecificationAudit(Integer pageNum, Integer pageSize, Specification specification);

    void updateStatus(Long[] ids, String status);
}
