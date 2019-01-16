package cn.itcast.core.service;

import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TypeTemplateDao typeTemplateDao;
    @Autowired
    private SpecificationOptionDao specificationOptionDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult searchTypeTemplate(Integer pageNum, Integer pageSize, TypeTemplate typeTemplate) {
        // 在初始化分类模板时,将所有的模板数据放入缓存库
        List<TypeTemplate> typeTemplates = selectAllTypeTemplates();

        if (redisTemplate.boundHashOps("specList").values() == null || redisTemplate.boundHashOps("specList").values().size() <= 0) {
            for (TypeTemplate template : typeTemplates) {
                // specList中应该包含规格和规格项,规格项的获取来自下面现成的方法
                redisTemplate.boundHashOps("specList").put(template.getId(), selectSpecListById(template.getId()));
                // 品牌只要包含id和text即可
                redisTemplate.boundHashOps("brandList").put(template.getId(), JSON.parseArray(template.getBrandIds(), Map.class));
            }
        }

        PageHelper.startPage(pageNum, pageSize);
        TypeTemplateQuery typeTemplateQuery = new TypeTemplateQuery();
        if (null != typeTemplate.getName() && !"".equals(typeTemplate.getName().trim())) {
            typeTemplateQuery.createCriteria().andNameLike("%" + typeTemplate.getName().trim() + "%");
        }
        Page<TypeTemplate> page = (Page<TypeTemplate>) typeTemplateDao.selectByExample(typeTemplateQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public TypeTemplate selectTypeTemplateById(Long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateTypeTemplate(TypeTemplate typeTemplate) {
        redisTemplate.boundHashOps("specList").delete(typeTemplate.getId());
        redisTemplate.boundHashOps("specList").put(typeTemplate.getId(), selectSpecListById(typeTemplate.getId()));
        redisTemplate.boundHashOps("brandList").delete(typeTemplate.getId());
        redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(), JSON.parseArray(typeTemplate.getBrandIds(), Map.class));
        return typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
    }

    @Override
    public int insertTypeTemplate(TypeTemplate typeTemplate) {
        redisTemplate.boundHashOps("specList").put(typeTemplate.getId(), selectSpecListById(typeTemplate.getId()));
        redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(), JSON.parseArray(typeTemplate.getBrandIds(), Map.class));
        return typeTemplateDao.insertSelective(typeTemplate);
    }

    @Override
    public int deleteTypeTemplateByIds(Long[] ids) {
        for (Long id : ids) {
            redisTemplate.boundHashOps("specList").delete(id);
            redisTemplate.boundHashOps("brandList").delete(id);
        }
        TypeTemplateQuery typeTemplateQuery = new TypeTemplateQuery();
        typeTemplateQuery.createCriteria().andIdIn(Arrays.asList(ids));
        return typeTemplateDao.deleteByExample(typeTemplateQuery);
    }

    @Override
    public List<TypeTemplate> selectAllTypeTemplates() {
        return typeTemplateDao.selectByExample(null);
    }

    @Override
    public List<Map> selectSpecListById(Long id) {
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        List<Map> maps = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
        for (Map map : maps) {
            SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
            specificationOptionQuery.createCriteria().andSpecIdEqualTo((long) (Integer) map.get("id"));
            List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(specificationOptionQuery);
            map.put("options", specificationOptions);
        }
        return maps;
    }

    @Override
    public List<Map> selectAllTypeTemplatesMap() {
        return typeTemplateDao.selectAllTypeTemplatesMap();
    }
}
