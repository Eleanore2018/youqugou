package cn.itcast.core.controller;

/**
 * 贾运通-2018/12/31
 */

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.TypeTemplateAuditService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/typeTemplateAudit")
public class TypeTemplateAuditController {

    @Reference
    private TypeTemplateAuditService typeTemplateAuditService;

    @RequestMapping("/searchTypeTemplateAudit")
    public PageResult searchTypeTemplateAudit(Integer pageNum, Integer pageSize, @RequestBody TypeTemplate typeTemplate) {
        return typeTemplateAuditService.searchTypeTemplateAudit(pageNum, pageSize, typeTemplate);
    }


    /**
     * 审核模板
     * @return
     */
    @RequestMapping("/updateStatus")
    public cn.itcast.core.entity.Result updateStatus(Long[] ids, String status) {
        try {
            typeTemplateAuditService.updateStatus(ids,status);
            return new cn.itcast.core.entity.Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new cn.itcast.core.entity.Result(false,"失败");
        }
    }

}
