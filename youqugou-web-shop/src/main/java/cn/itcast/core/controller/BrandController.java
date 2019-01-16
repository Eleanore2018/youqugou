/**
 * 这一页有海贼王王浩宇修改的地方
 */
package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查询所有brand
     * @return
     */


@RequestMapping("/selectAllBrandMap")
    public List<Map> selectAllBrandMap() {
        return brandService.selectAllBrandMap();
    }

    /**
     * 查询分页数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectPageBrand")
    public PageResult selectPageBrand(Integer pageNum, Integer pageSize) {
        return brandService.selectPageBrand(pageNum, pageSize);
    }

    /**
     * 王浩宇修改此处了2018/12/29
     */
    //-----------------------------------------------------
    @RequestMapping("/insertBrand")
    public Result insertBrand(@RequestBody Brand brand) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        brand.setSellername(user.getUsername());
        brand.setStatus("0");
        int row = brandService.insertBrand(brand);
        if (row > 0) {
            return new Result(true, "保存成功");
        }
        return new Result(false, "保存失败");
    }
    //--------------------------------------------------------
    /**
     * 修改brand
     * @param brand
     * @return
     */
    @RequestMapping("/updateBrand")
    public Result updateBrand(@RequestBody Brand brand) {
        brand.setStatus("0");
        int row = brandService.updateBrand(brand);
        if (row > 0) {
            return new Result(true,"修改成功");
        }
        return new Result(false, "修改失败");
    }

    /**
     * 根据主键查找brand
     * @param id
     * @return
     */
    @RequestMapping("/selectBrandById")
    public Brand selectBrandById(Long id) {
        return brandService.selectBrandById(id);
    }

    /**
     * 根据ids批量删除brand
     * @param ids
     * @return
     */
    @RequestMapping("/deleteBrandByIds")
    public Result deleteBrandByIds(@RequestBody Long[] ids) {
        int row = brandService.deleteBrandByIds(ids);
        if (row > 0) {
            return new Result(true,"删除成功");
        }
        return new Result(false, "删除失败");
    }

    /**
     * 王浩宇修改此处于2018/12/30,品牌搜索需要根据当前登陆上家搜索
     */
    @RequestMapping("/searchBrand")
    public PageResult searchBrand(Integer pageNum, Integer pageSize, @RequestBody Brand brand) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //商品对象补充当前用户信息
        return brandService.searchBrand(pageNum, pageSize, brand,name);
    }
}

