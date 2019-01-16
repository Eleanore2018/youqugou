package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.BrandAuditService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 贾运通2018/12/30
 */

@RestController
@RequestMapping("/brandAudit")
public class BrandAuditController {

    @Reference
    private BrandAuditService brandAuditService;

    /**
     * 查询分页数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/searchBrandAudit")
    public PageResult searchBrandAudit(Integer pageNum, Integer pageSize,@RequestBody Brand brand) {
        return brandAuditService.searchBrandAudit(pageNum, pageSize, brand);
    }


    /**
     * 品牌审核
     * @param ids
     * @param status
     * @return
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            brandAuditService.updateStatus(ids,status);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }

}
