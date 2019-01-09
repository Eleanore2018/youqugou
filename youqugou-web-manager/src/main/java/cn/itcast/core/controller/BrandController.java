package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
     * 插入数据
     */
    @RequestMapping("/insertBrand")
    public Result insertBrand(@RequestBody Brand brand) {
        int row = brandService.insertBrand(brand);
        if (row > 0) {
            return new Result(true, "保存成功");
        }
        return new Result(false, "保存失败");
    }

    /**
     * 修改brand
     * @param brand
     * @return
     */
    @RequestMapping("/updateBrand")
    public Result updateBrand(@RequestBody Brand brand) {
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
     * 查询分页数据,带查询条件
     * @param pageNum
     * @param pageSize
     * @param brand
     * @return
     */
    @RequestMapping("/searchBrand")
    public PageResult searchBrand(Integer pageNum, Integer pageSize, @RequestBody Brand brand) {
        return brandService.searchBrand(pageNum, pageSize, brand);
    }


    /*贾运通*/
    //品牌审核
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            brandService.updateStatus(ids,status);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }
}

