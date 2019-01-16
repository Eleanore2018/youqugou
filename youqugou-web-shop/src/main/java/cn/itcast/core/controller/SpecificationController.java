package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojogroup.SpecificationVo;
import cn.itcast.core.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    // 远程调用service层对象
    @Reference
    private SpecificationService specificationService;

    /**
     * 根据条件查找分页数据
     * @param pageNum
     * @param pageSize
     * @param specification
     * @return
     */
    @RequestMapping("/searchSpecification")
    public PageResult searchSpecification(Integer pageNum, Integer pageSize, @RequestBody Specification specification) {
        PageResult pageResult = specificationService.searchSpecification(pageNum, pageSize, specification);
        return pageResult;
    }

    /**
     * 封装SpecificationVo对象
     * @param id
     * @return
     */
    @RequestMapping("/selectSpecificationVoById")
    public SpecificationVo selectSpecificationVoById(Long id) {
        return specificationService.selectSpecificationVoById(id);
    }

    /**
     * 新增SpecificationVo数据
     * @param specificationVo
     * @return
     */
    @RequestMapping("/insertSpecificationVo")
    public Result insertSpecificationVo(@RequestBody SpecificationVo specificationVo) {
        specificationVo.getSpecification().setStatus("0");
        int row = specificationService.insertSpecificationVo(specificationVo);
        if (row > 0) {
            return new Result(true, "成功");
        }
        return new Result(false, "失败");
    }

    /**
     * 修改SpecificationVo数据
     * @param specificationVo
     * @return
     */
    @RequestMapping("/updateSpecificationVo")
    public Result updateSpecificationVo(@RequestBody SpecificationVo specificationVo) {
        specificationVo.getSpecification().setStatus("0");
        int row = specificationService.updateSpecificationVo(specificationVo);
        if (row > 0) {
            return new Result(true, "成功");
        }
        return new Result(false, "失败");
    }

    /**
     * 删除SpecificationVo数据
     * @param ids
     * @return
     */
    @RequestMapping("/deleteSpecificationVo")
    public Result deleteSpecificationVo(@RequestBody Long[] ids) {
        int row = specificationService.deleteSpecificationVo(ids);
        if (row > 0) {
            return new Result(true, "成功");
        }
        return new Result(false, "失败");
    }

    @RequestMapping("/selectAllSpecificationMap")
    public List<Map> selectAllSpecificationMap() {
        return specificationService.selectAllSpecificationMap();
    }
}
