package cn.itcast.core.service;


import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojogroup.SpecificationVo;


import java.util.List;
import java.util.Map;

public interface SpecificationService {
    PageResult searchSpecification(Integer pageNum, Integer pageSize, Specification specification);

    SpecificationVo selectSpecificationVoById(Long id);

    int insertSpecificationVo(SpecificationVo specificationVo);

    int updateSpecificationVo(SpecificationVo specificationVo);

    int deleteSpecificationVo(Long[] ids);

    List<Map> selectAllSpecificationMap();
}
