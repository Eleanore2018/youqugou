package cn.itcast.core.controller;

import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.service.ContentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;

    @RequestMapping("/selectContentsByCategoryId")
    public List<Content> selectContentsByCategoryId(Long categoryId) {
        return contentService.selectContentsByCategoryId(categoryId);
    }

    /**
     * 查询楼层广告信息
     * @author 陈伟鑫
     * @date 12.28
     * @param contentCategory
     * @return
     */
    @RequestMapping("/findFloorContent")
    public Map<String,Map> findFloorContent(@RequestBody Long[] contentCategory) {
        Map<String,Map> map = contentService.findFloorContent(contentCategory);
        return map;
    }
}
