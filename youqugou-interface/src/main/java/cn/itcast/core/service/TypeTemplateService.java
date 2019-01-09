package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {
    PageResult searchTypeTemplate(Integer pageNum, Integer pageSize, TypeTemplate typeTemplate);

    TypeTemplate selectTypeTemplateById(Long id);

    int updateTypeTemplate(TypeTemplate typeTemplate);

    int insertTypeTemplate(TypeTemplate typeTemplate);

    int deleteTypeTemplateByIds(Long[] ids);

    List<TypeTemplate> selectAllTypeTemplates();

    List<Map> selectSpecListById(Long id);

    List<Map> selectAllTypeTemplatesMap();
}
