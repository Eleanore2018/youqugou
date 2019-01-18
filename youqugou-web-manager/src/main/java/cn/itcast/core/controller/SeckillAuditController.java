package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillAuditService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckillAudit")
public class SeckillAuditController {

    @Reference
    private SeckillAuditService seckillAuditService;

    @RequestMapping("/searchSecKillAudit")
    public PageResult searchSecKillAudit(Integer pageNum, Integer pageSize, @RequestBody SeckillGoods seckillGoods) {
        PageResult pageResult = seckillAuditService.searchSeckillAudit(pageNum, pageSize, seckillGoods);
        return pageResult;
    }


    /**
     * 秒杀审核
     * @return
     */
    @RequestMapping("/updateStatus")
    public cn.itcast.core.entity.Result updateStatus(Long[] ids, String status) {
        try {
            seckillAuditService.updateStatus(ids,status);
            return new cn.itcast.core.entity.Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new cn.itcast.core.entity.Result(false,"失败");
        }
    }
}
