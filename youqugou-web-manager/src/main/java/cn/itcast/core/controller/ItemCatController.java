package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("/searchItemCat")
    public PageResult searchItemCat(Integer pageNum, Integer pageSize, @RequestBody ItemCat itemcat) {
        return itemCatService.searchItemCat(pageNum, pageSize, itemcat);
    }

    @RequestMapping("/insertItemCat")
    public Result insertItemCat(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.insertItemCat(itemCat);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }

    @RequestMapping("/updateItemCat")
    public Result updateItemCat(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.updateItemCat(itemCat);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }

    /**
     * 查询所有商品类目信息
     * @return
     */
    @RequestMapping("/selectAllItemCats")
    public List<ItemCat> selectAllItemCats() {
        return itemCatService.selectAllItemCats();
    }

    /**
     * 查询所有商品的类目信息(id,name as text) 为select2准备
     * @param parentId
     * @return
     */
    @RequestMapping("/selectItemCatsMapByParentId")
    public List<Map> selectItemCatsMapByParentId(Long parentId) {
        return itemCatService.selectItemCatsMapByParentId(parentId);
    }

    @RequestMapping("/selectItemCatById")
    public ItemCat selectItemCatById(Long id) {
        return itemCatService.selectItemCatById(id);
    }
}
