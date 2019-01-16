package cn.itcast.core.pojogroup;

import cn.itcast.core.pojo.order.OrderItem;

import java.io.Serializable;
import java.util.Date;


/**
 * 订单详情显示页面包装对象 张静 2018-12-29
 */
public class OrderItemVo implements Serializable{
    //地址
    private String addr;
    private OrderItem orderItem;   //订单详情
    private Date createTime;    //下单时间
    private Date paymentTime;   //支付时间
    private String paymentType;  //支付方式
    private Date congignTime;     //发货时间
    private String orderIdStr;

    public String getOrderIdStr() {
        return orderIdStr;
    }

    public void setOrderIdStr(String orderIdStr) {
        this.orderIdStr = orderIdStr;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getCongignTime() {
        return congignTime;
    }

    public void setCongignTime(Date congignTime) {
        this.congignTime = congignTime;
    }
}
