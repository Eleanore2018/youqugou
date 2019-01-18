package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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



    @RequestMapping("/selectItemCatsMapByParentId")
    public List<Map> selectItemCatsMapByParentId(Long parentId) {
        return itemCatService.selectItemCatsMapByParentId(parentId);
    }
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

}
