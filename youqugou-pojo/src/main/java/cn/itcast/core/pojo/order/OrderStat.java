package cn.itcast.core.pojo.order;

import java.io.Serializable;

/**
 * Author: Mr Liu
 * Date: 2019/1/2 01:02
 */
public class OrderStat implements Serializable {
    private String sellerId;
    private String sellerName;
    private Double payment;
    private Long goodsId;
    private Long category1Id;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getCategory1Id() {
        return category1Id;
    }

    public void setCategory1Id(Long category1Id) {
        this.category1Id = category1Id;
    }
}
