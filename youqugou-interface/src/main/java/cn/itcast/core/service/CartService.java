package cn.itcast.core.service;

import cn.itcast.core.pojo.Cart;

import java.util.List;

public interface CartService {
    Cart selectCart(Long itemId, Integer num);

    List<Cart> fillCarts(List<Cart> carts);

    void mergeCart(List<Cart> carts, String username);

    List<Cart> selectCartsFromRedis(String username);
}
