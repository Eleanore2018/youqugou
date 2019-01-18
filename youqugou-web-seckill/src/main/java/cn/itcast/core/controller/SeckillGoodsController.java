package cn.itcast.core.controller;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 秒杀商品管理
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

    @Reference
    private SeckillGoodsService seckillGoodsService;

    //查询当前秒杀商品列表  张静 2018-12-31
    @RequestMapping("/findSeckillList")
    public List<SeckillGoods> findSeckillList(){
        return seckillGoodsService.findSeckillList();
    }


    //查询指定的秒杀商品列表
    @RequestMapping("/findOne")
    public SeckillGoods findOne(Long id){
        return  seckillGoodsService.findOne(id);
    }


    //实现商品秒杀 张静 2019-01-01
    @RequestMapping("/goToSeckill")
    public Result goToSeckill(Long id){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            boolean flag=seckillGoodsService.goToSeckill(name,id);
            if (flag){
                return new Result(true,"秒杀成功,请确认信息!");
            }else {
                return new Result(false,"秒杀失败!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"系统错误!");
        }
    }

}
