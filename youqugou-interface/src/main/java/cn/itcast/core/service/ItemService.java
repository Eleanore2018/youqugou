package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.Item;


public interface ItemService {
    PageResult searchitemPage(Integer pageNum, Integer pageSize, Item item);
}
