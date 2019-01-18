package cn.itcast.core.pojogroup;

import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;

import java.io.Serializable;
import java.util.Date;


/**
 * 秒杀订单包装对象 张静 2019-01-01
 */
public class SeckillOrderVo implements Serializable{

    private SeckillOrder seckillOrder;  //秒杀订单
    private String goodsName;    //秒杀商品名称
    private String buyerNick;   //店铺
    private String specStr;    //规格信息

    public SeckillOrder getSeckillOrder() {
        return seckillOrder;
    }

    public void setSeckillOrder(SeckillOrder seckillOrder) {
        this.seckillOrder = seckillOrder;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public String getSpecStr() {
        return specStr;
    }

    public void setSpecStr(String specStr) {
        this.specStr = specStr;
    }
}
