package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    @RequestMapping("/selectTypeTemplateById")
    public TypeTemplate selectTypeTemplateById(Long id) {
        return typeTemplateService.selectTypeTemplateById(id);
    }

    @RequestMapping("/searchTypeTemplate")
    public PageResult searchTypeTemplate(Integer pageNum, Integer pageSize, @RequestBody TypeTemplate typeTemplate) {
        return typeTemplateService.searchTypeTemplate(pageNum, pageSize, typeTemplate);
    }

    @RequestMapping("/updateTypeTemplate")
    public Result updateTypeTemplate(@RequestBody TypeTemplate typeTemplate) {
        int row = typeTemplateService.updateTypeTemplate(typeTemplate);
        if (row > 0) {
            return new Result(true, "成功");
        }
        return new Result(false, "失败");
    }

    @RequestMapping("/insertTypeTemplate")
    public Result insertTypeTemplate(@RequestBody TypeTemplate typeTemplate) {
        int row = typeTemplateService.insertTypeTemplate(typeTemplate);
        if (row > 0) {
            return new Result(true, "成功");
        }
        return new Result(false, "失败");
    }

    @RequestMapping("/deleteTypeTemplateByIds")
    public Result deleteTypeTemplateByIds(@RequestBody Long[] ids) {
        if (ids != null){
            int row = typeTemplateService.deleteTypeTemplateByIds(ids);
            if (row > 0) {
                return new Result(true, "成功");
            } else {
                return new Result(false, "失败");
            }
        }
        return new Result(false, "失败");
    }

    @RequestMapping("/selectAllTypeTemplates")
    public List<TypeTemplate> selectAllTypeTemplates() {
        return typeTemplateService.selectAllTypeTemplates();
    }

    @RequestMapping("/selectAllTypeTemplatesMap")
    public List<Map> selectAllTypeTemplatesMap() {
        return typeTemplateService.selectAllTypeTemplatesMap();
    }
}
