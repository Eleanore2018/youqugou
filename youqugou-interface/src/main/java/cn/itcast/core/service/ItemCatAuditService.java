package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;
import java.util.Map;

public interface ItemCatAuditService {
    PageResult searchItemCatAudit(Integer pageNum, Integer pageSize, ItemCat itemcat);

    void updateStatus(Long[] ids, String status);

    void insertItemCat(ItemCat itemCat);

    void updateItemCat(ItemCat itemCat);

    List<ItemCat> selectAllItemCats();

    List<Map> selectItemCatsMapByParentId(Long parentId);

    ItemCat selectItemCatById(Long id);

    List<ItemCat> selectItemCatsByParentId(Long parentId);
}
