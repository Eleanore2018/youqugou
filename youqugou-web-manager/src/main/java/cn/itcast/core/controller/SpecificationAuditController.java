package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.service.SpecificationAuditService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specificationAudit")
public class SpecificationAuditController {

    // 远程调用service层对象
    @Reference
    private SpecificationAuditService specificationAuditService;

    /**
     * 根据条件查找分页数据
     * @param pageNum
     * @param pageSize
     * @param specification
     * @return
     */
    @RequestMapping("/searchSpecificationAudit")
    public PageResult searchSpecificationAudit(Integer pageNum, Integer pageSize, @RequestBody Specification specification) {
        return specificationAuditService.searchSpecificationAudit(pageNum, pageSize, specification);
    }

    /**
     * 审核规格
     * @return
     */
    @RequestMapping("/updateStatus")
    public cn.itcast.core.entity.Result updateStatus(Long[] ids, String status) {
        try {
            specificationAuditService.updateStatus(ids,status);
            return new cn.itcast.core.entity.Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new cn.itcast.core.entity.Result(false,"失败");
        }
    }
}
