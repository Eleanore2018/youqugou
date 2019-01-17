package cn.itcast.core.controller;

import cn.itcast.core.service.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/*
* 左建洲
* */
@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {
    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    public Map<String, Object> search(@RequestBody Map<String, String> searchMap) {
        Map<String, Object> map = itemSearchService.searchItem(searchMap);

        return map;
    }
}
