package cn.itcast.core.pojo.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem implements Serializable {
    private Long id;


    //张静 开始 2018-12-29
    private String idStr;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }
    //张静 结束 2018-12-29
    /**
     * 订单详情状态
     */
    private String status;

    /**
     * 商品id
     */
    private Long itemId;

    /**
     * SPU_ID
     */
    private Long goodsId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 商品购买数量
     */
    private Integer num;

    /**
     * 商品总金额
     */
    private BigDecimal totalFee;

    /**
     * 商品图片地址
     */
    private String picPath;


    //商品名称
    private String goodsName;

    //规格
    private String specStr;

    public String getSpecStr() {
        return specStr;
    }

    public void setSpecStr(String specStr) {
        this.specStr = specStr;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    private String sellerId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath == null ? null : picPath.trim();
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId == null ? null : sellerId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", itemId=").append(itemId);
        sb.append(", goodsId=").append(goodsId);
        sb.append(", orderId=").append(orderId);
        sb.append(", title=").append(title);
        sb.append(", price=").append(price);
        sb.append(", num=").append(num);
        sb.append(", totalFee=").append(totalFee);
        sb.append(", picPath=").append(picPath);
        sb.append(", sellerId=").append(sellerId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(itemId, orderItem.itemId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(itemId);
    }
}