package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;

import cn.itcast.core.pojo.item.Item;

import cn.itcast.core.service.ItemService;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Reference
    private ItemService itemService;



    /**
     * @return
     */
    @RequestMapping("/searchitemPage")
    public PageResult searchitemPage(Integer pageNum, Integer pageSize, @RequestBody Item item) {
        System.out.println(1);
        System.out.println(2);
        System.out.println(3);
        return itemService.searchitemPage(pageNum,pageSize,item);
    }


}
