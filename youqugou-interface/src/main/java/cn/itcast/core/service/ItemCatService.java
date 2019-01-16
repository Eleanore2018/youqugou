package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;
import java.util.Map;

public interface ItemCatService {

    List<ItemCat> selectItemCatsByParentId(Long parentId);

    ItemCat selectItemCatById(Long id);

    void updateItemCat(ItemCat itemCat);

    PageResult searchItemCat(Integer pageNum, Integer pageSize, ItemCat itemcat);

    void insertItemCat(ItemCat itemCat);

    List<ItemCat> selectAllItemCats();

    List<Map> selectItemCatsMapByParentId(Long parentId);
}
