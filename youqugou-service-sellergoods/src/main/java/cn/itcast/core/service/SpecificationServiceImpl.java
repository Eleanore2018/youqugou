package cn.itcast.core.service;

import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;

import cn.itcast.core.pojogroup.SpecificationVo;
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
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecificationDao specificationDao;
    @Autowired
    private SpecificationOptionDao specificationOptionDao;

    @Override
    public PageResult searchSpecification(Integer pageNum, Integer pageSize, Specification specification) {
        // 分页开始
        PageHelper.startPage(pageNum, pageSize);
        // 设定执行查找条件
        SpecificationQuery specificationQuery = new SpecificationQuery();
        SpecificationQuery.Criteria criteria = specificationQuery.createCriteria();
        if (null != specification.getSpecName() && !"".equals(specification.getSpecName())){
            criteria.andSpecNameLike("%" + specification.getSpecName().trim() + "%");
        }
        // 根据设置的条件执行查找
        Page<Specification> page = (Page<Specification>) specificationDao.selectByExample(specificationQuery);
        // 返回需要的分页数据
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public SpecificationVo selectSpecificationVoById(Long id) {
        SpecificationVo specificationVo = new SpecificationVo();
        // 根据规格id查询规格对象
        Specification specification = specificationDao.selectByPrimaryKey(id);
        specificationVo.setSpecification(specification);

        // 设定执行查找条件
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = specificationOptionQuery.createCriteria();
        criteria.andSpecIdEqualTo(id);
        // 排序方式按照orders的升序
        specificationOptionQuery.setOrderByClause("orders asc");

        List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(specificationOptionQuery);
        specificationVo.setSpecificationOptions(specificationOptions);
        return specificationVo;
    }

    @Override
    public int insertSpecificationVo(SpecificationVo specificationVo) {
        int row = specificationDao.insertSelective(specificationVo.getSpecification());

        // specification与specificationOption是一对多关系,需要遍历依次插入数据
        specificationVo.getSpecificationOptions().forEach(specificationOption -> {
            specificationOption.setSpecId(specificationVo.getSpecification().getId());
            specificationOptionDao.insertSelective(specificationOption);
        });
        return row;
    }

    @Override
    public int updateSpecificationVo(SpecificationVo specificationVo) {
        int row = specificationDao.updateByPrimaryKeySelective(specificationVo.getSpecification());

        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        specificationOptionQuery.createCriteria().andSpecIdEqualTo(specificationVo.getSpecification().getId());
        // 先删除
        specificationOptionDao.deleteByExample(specificationOptionQuery);

        List<SpecificationOption> specificationOptions = specificationVo.getSpecificationOptions();
        specificationOptions.forEach(specificationOption -> {
            specificationOption.setSpecId(specificationVo.getSpecification().getId());
            // 再新建
            specificationOptionDao.insertSelective(specificationOption);
        });
        return row;
    }

    @Override
    public int deleteSpecificationVo(Long[] ids) {
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        specificationOptionQuery.createCriteria().andSpecIdIn(Arrays.asList(ids));
        specificationOptionDao.deleteByExample(specificationOptionQuery);

        // specification与specificationOption是一对多关系,需要遍历依次删除数据
        SpecificationQuery specificationQuery = new SpecificationQuery();
        specificationQuery.createCriteria().andIdIn(Arrays.asList(ids));
        int row = specificationDao.deleteByExample(specificationQuery);
        return row;
    }

    @Override
    public List<Map> selectAllSpecificationMap() {
        return specificationDao.selectAllSpecificationMap();
    }
}
