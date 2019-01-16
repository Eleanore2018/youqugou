package cn.itcast.core.service;

import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 贾运通-2018/12/31
 */

@Service
@Transactional
public class TypeTemplateAuditServiceImpl implements TypeTemplateAuditService {

    @Autowired
    private TypeTemplateDao typeTemplateDao;

    @Override
    public PageResult searchTypeTemplateAudit(Integer pageNum, Integer pageSize, TypeTemplate typeTemplate) {
        PageHelper.startPage(pageNum, pageSize);

        TypeTemplateQuery typeTemplateQuery = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = typeTemplateQuery.createCriteria();
        criteria.andStatusEqualTo("0");//只查询未审核的
        if (null != typeTemplate.getName() && !"".equals(typeTemplate.getName().trim())) {
            if (typeTemplate.getName().contains(" ")){
                typeTemplate.setName(typeTemplate.getName().replace(" ",""));
            }
            criteria.andNameLike("%" + typeTemplate.getName().trim() + "%");
        }
        Page<TypeTemplate> page = (Page<TypeTemplate>) typeTemplateDao.selectByExample(typeTemplateQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }




    @Override
    public void updateStatus(Long[] ids, String status) {

        TypeTemplate typeTemplate = new TypeTemplate();
        typeTemplate.setStatus(status);
        for (Long id : ids) {
                typeTemplate.setId(id);
                typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
            }
        }
    }

