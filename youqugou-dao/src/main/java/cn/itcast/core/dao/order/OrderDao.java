package cn.itcast.core.dao.order;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.order.OrderStat;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDao {
    int countByExample(OrderQuery example);

    int deleteByExample(OrderQuery example);

    int deleteByPrimaryKey(Long orderId);

    int insert(Order record);

    int insertSelective(Order record);

    List<Order> selectByExample(OrderQuery example);

    Order selectByPrimaryKey(Long orderId);

    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderQuery example);

    int updateByExample(@Param("record") Order record, @Param("example") OrderQuery example);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Object selectOrdersByUserId(String name);

    /**
     * Author:Mr Liu
     * Date:2019/1/1
     * use to query payment list
     */
    List<BigDecimal> selectPaymentByExample(OrderQuery orderQuery);
}