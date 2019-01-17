package cn.itcast.core.controller;

import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
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

    @RequestMapping("/selectSpecListById")
    public List<Map> selectSpecListById(Long id) {
        return typeTemplateService.selectSpecListById(id);
    }
}