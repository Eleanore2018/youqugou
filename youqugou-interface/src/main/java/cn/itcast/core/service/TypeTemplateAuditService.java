package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.template.TypeTemplate;

public interface TypeTemplateAuditService {
    PageResult searchTypeTemplateAudit(Integer pageNum, Integer pageSize, TypeTemplate typeTemplate);

    void updateStatus(Long[] ids, String status);
}
