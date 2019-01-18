package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatAuditService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 贾运通 2018/12/31
 */

@RestController
@RequestMapping("/itemCatAudit")
public class ItemCatAuditController {

    @Reference
    private ItemCatAuditService itemCatAuditService;

    @RequestMapping("/searchItemCatAudit")
    public PageResult searchItemCatAudit(Integer pageNum, Integer pageSize, @RequestBody ItemCat itemcat) {
        return itemCatAuditService.searchItemCatAudit(pageNum, pageSize, itemcat);
    }


    /**
     * 审核分类
     * @return
     */
    @RequestMapping("/updateStatus")
    public cn.itcast.core.entity.Result updateStatus(Long[] ids, String status) {
        try {
            itemCatAuditService.updateStatus(ids,status);
            return new cn.itcast.core.entity.Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new cn.itcast.core.entity.Result(false,"失败");
        }
    }



    @RequestMapping("/insertItemCat")
    public Result insertItemCat(@RequestBody ItemCat itemCat) {
        try {
            itemCatAuditService.insertItemCat(itemCat);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }

    @RequestMapping("/updateItemCat")
    public Result updateItemCat(@RequestBody ItemCat itemCat) {
        try {
            itemCatAuditService.updateItemCat(itemCat);
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
        return itemCatAuditService.selectAllItemCats();
    }

    /**
     * 查询所有商品的类目信息(id,name as text) 为select2准备
     * @param parentId
     * @return
     */
    @RequestMapping("/selectItemCatsMapByParentId")
    public List<Map> selectItemCatsMapByParentId(Long parentId) {
        return itemCatAuditService.selectItemCatsMapByParentId(parentId);
    }

    @RequestMapping("/selectItemCatById")
    public ItemCat selectItemCatById(Long id) {
        return itemCatAuditService.selectItemCatById(id);
    }

}
