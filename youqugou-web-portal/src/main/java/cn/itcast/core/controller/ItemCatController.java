package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;
    /**
     * 查询商城首页分类信息
     * @author 陈伟鑫
     * @Date 12.29
     * @param parentId
     * @return
     */
    @RequestMapping("/findAllItemCat")
    public List<ItemCat> findAllItemCat(Long parentId) {
        return itemCatService.findAllItemCat(parentId);
    }
}
