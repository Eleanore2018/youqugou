package cn.itcast.core.service;

import cn.itcast.core.pojo.item.Item;

import java.util.List;

public interface Itemservice {
    List<Item> findItemMapByGoodsId(Long id);
}
