package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 构建购物车项
     *
     * @param itemId
     * @param num
     * @return
     */
    @Override
    public Cart selectCart(Long itemId, Integer num) {
        Cart cart = new Cart();
        Item item = itemDao.selectByPrimaryKey(itemId);
        // 只设置商家id即可,不用设置商家名称
        cart.setSellerId(item.getSellerId());

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(itemId);
        orderItem.setNum(num);
        orderItems.add(orderItem);

        // 设置订单项集合(在添加购物车时实际上只有一个)
        cart.setOrderItems(orderItems);
        return cart;
    }

    /**
     * 填充购物车结果集的数据
     *
     * @param carts
     * @return
     */
    @Override
    public List<Cart> fillCarts(List<Cart> carts) {
        // 初始化item
        Item item = null;
        for (Cart cart : carts) {
            for (OrderItem orderItem : cart.getOrderItems()) {
                item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                // 设置图片路径
                orderItem.setPicPath(item.getImage());
                // 设置标题
                orderItem.setTitle(item.getTitle());
                // 设置价格
                orderItem.setPrice(item.getPrice());
                // 设置小计
                orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * orderItem.getNum()));
            }
            // 设置商家名称
            cart.setSellerName(item.getSeller());
        }
        return carts;
    }

    /**
     * 合并缓存和Cookie的购物车数据
     * @param carts
     * @param username
     */
    @Override
    public void mergeCart(List<Cart> carts, String username) {
        // 1 查询用户缓存中的购物车数据
        List<Cart> oldCarts = (List<Cart>) redisTemplate.boundHashOps("cart").get(username);
        if (null != oldCarts && oldCarts.size() > 0) {
            // 2 缓存有数据
            for (Cart cart : carts) {
                // 查询结果集中是否已存在此商家
                int cartIndex = oldCarts.indexOf(cart);
                if (cartIndex != -1) {
                    // 表示购物车结果集中存在此商家
                    List<OrderItem> orderItems = oldCarts.get(cartIndex).getOrderItems();
                    for (OrderItem newOrderItem : cart.getOrderItems()) {
                        int orderItemIndex = orderItems.indexOf(newOrderItem);
                        // 继续判断是否存在此订单项
                        if (orderItemIndex != -1) {
                            // 表示存在此订单项,只需要追加购买数量即可
                            OrderItem oldOrderItem = orderItems.get(orderItemIndex);
                            oldOrderItem.setNum(oldOrderItem.getNum() + newOrderItem.getNum());
                        } else {
                            // 表示不存在此订单项,追加此订单项即可
                            orderItems.add(newOrderItem);
                        }
                    }
                } else {
                    // 表示购物车结果集中不存在此商家
                    // 直接添加即可
                    oldCarts.add(cart);
                }
            }
            redisTemplate.boundHashOps("cart").put(username, oldCarts);
        } else {
            // 3 缓存没数据
            redisTemplate.boundHashOps("cart").put(username, carts);
        }
    }

    @Override
    public List<Cart> selectCartsFromRedis(String username) {
        return fillCarts((List<Cart>) redisTemplate.boundHashOps("cart").get(username));
    }
}
