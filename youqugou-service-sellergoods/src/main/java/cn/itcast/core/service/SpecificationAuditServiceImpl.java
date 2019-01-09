package cn.itcast.core.service;

import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SpecificationAuditServiceImpl implements SpecificationAuditService {

    @Autowired
    private SpecificationDao specificationDao;

    @Override
    public PageResult searchSpecificationAudit(Integer pageNum, Integer pageSize, Specification specification) {
        // 分页开始
        PageHelper.startPage(pageNum, pageSize);
        // 设定执行查找条件
        SpecificationQuery specificationQuery = new SpecificationQuery();
        SpecificationQuery.Criteria criteria = specificationQuery.createCriteria();
        criteria.andStatusEqualTo("0");//只查询未审核的
        if (null != specification.getSpecName() && !"".equals(specification.getSpecName())){
            criteria.andSpecNameLike("%" + specification.getSpecName().trim() + "%");
        }
        // 根据设置的条件执行查找
        Page<Specification> page = (Page<Specification>) specificationDao.selectByExample(specificationQuery);
        // 返回需要的分页数据
        return new PageResult(page.getTotal(), page.getResult());
    }


    //规格审核
    @Override
    public void updateStatus(Long[] ids, String status) {
        Specification specification = new Specification();
        specification.setStatus(status);
            for (Long id : ids) {
                specification.setId(id);
                specificationDao.updateByPrimaryKeySelective(specification);
            }
        }
    }

