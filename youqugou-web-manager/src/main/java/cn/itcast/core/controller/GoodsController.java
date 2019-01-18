package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojogroup.GoodsVo;
import cn.itcast.core.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private GoodsService goodsService;

    /**
     * 插入商品信息
     * @param goodsVo
     * @return
     */
    @RequestMapping("/insertGoodsVo")
    public Result insertGoodsVo(@RequestBody GoodsVo goodsVo) {
        try {
            // 获取商家id
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsVo.getGoods().setSellerId(sellerId);
            goodsService.insertGoodsVo(goodsVo);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }

    /**
     * 查询分页(带查询条件)
     * @param pageNum
     * @param pageSize
     * @param goods
     * @return
     */
    @RequestMapping("/searchGoodsPage")
    public PageResult searchGoodsPage(Integer pageNum, Integer pageSize, @RequestBody Goods goods) {
        return goodsService.searchGoodsPage(pageNum, pageSize, goods);
    }

    /**
     * 根据商品id查询GoodsVo
     * @param id
     * @return
     */
    @RequestMapping("/selectGoodsVoById")
    public GoodsVo selectGoodsVoById(Long id) {
        return goodsService.selectGoodsVoById(id);
    }

    /**
     * 修改GoodsVo
     * @param goodsVo
     * @return
     */
    @RequestMapping("/updateGoodsVo")
    public Result updateGoodsVo(@RequestBody GoodsVo goodsVo) {
        try {
            goodsService.updateGoodsVo(goodsVo);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }

    /**
     * 修改审核状态
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }

    /**
     * 删除GoodsVo,实际上是修改is_delete标记,不真删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }
}
