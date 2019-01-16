package cn.itcast.core.service;

import cn.itcast.core.pojo.item.Item;

import java.util.List;
import java.util.Map;

public interface Itemservice {
    List<Item> findItemMapByGoodsId(Long id);
}
