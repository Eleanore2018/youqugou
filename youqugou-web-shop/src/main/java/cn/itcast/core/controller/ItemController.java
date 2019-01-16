package cn.itcast.core.controller;


import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.service.Itemservice;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Reference
    private Itemservice itemservice;

    @RequestMapping("/findItemMapByGoodsId")
    public List<Item> findItemMapByGoodsId(Long id){
        return itemservice.findItemMapByGoodsId(id);
    }
}
