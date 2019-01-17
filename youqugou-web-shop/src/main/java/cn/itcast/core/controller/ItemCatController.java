package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    /**
     * 根据父id查询类目信息
     * @param parentId
     * @return
     */
    @RequestMapping("/selectItemCatsByParentId")
    public List<ItemCat> selectItemCatsByParentId(Long parentId) {
        return itemCatService.selectItemCatsByParentId(parentId);
    }

    /**
     * 根据分类id查询商品类目信息
     * @param id
     * @return
     */
    @RequestMapping("/selectItemCatById")
    public ItemCat selectItemCatById(Long id) {
        return itemCatService.selectItemCatById(id);
    }

    /**
     * 查询所有商品类目信息
     * @return
     */
    @RequestMapping("/selectAllItemCats")
    public List<ItemCat> selectAllItemCats() {
        return itemCatService.selectAllItemCats();
    }
}
