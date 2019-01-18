package cn.itcast.core.controller;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference
    private CartService cartService;

    /**
     * 添加购物车
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @CrossOrigin(origins = "http://localhost:9003", allowCredentials = "true") // 允许跨域访问
    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Cart> carts = null;
            // 1 获取Cookie数组,取出购物车结果集
            Cookie[] cookies = request.getCookies();
            if (null != cookies && cookies.length >0) {
                for (Cookie cookie : cookies) {
                    if ("cart".equals(cookie.getName())) {
                        carts = JSON.parseArray(cookie.getValue(), Cart.class);
                        // 清空Cookie数据
                        cookie = new Cookie("cart", null);
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        break;
                    }
                }
            }
            // 2 获取当前款数据
            Cart cart = cartService.selectCart(itemId, num);
            if (carts == null) {
                // 3 结果集为空,则创建购物车集合,添加当前款
                carts = new ArrayList<>();
                carts.add(cart);
            } else {
                // 4 不为空,将当前款追加到结果集中
                // 查询结果集中是否已存在此商家
                int cartIndex = carts.indexOf(cart);
                if (cartIndex != -1) {
                    // 表示购物车结果集中存在此商家
                    List<OrderItem> orderItems = carts.get(cartIndex).getOrderItems();
                    OrderItem newOrderItem = cart.getOrderItems().get(0);
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
                } else {
                    // 表示购物车结果集中不存在此商家
                    // 直接添加即可
                    carts.add(cart);
                }
            }

            // 判断用户是否登录
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            // 未登录时会生成一个匿名用户,匿名用户默认为anonymousUser
            if (!"anonymousUser".equals(username)) {
                // 已登录,添加到缓存
                // 合并缓存和Cookie中的购物车结果集
                cartService.mergeCart(carts, username);
            } else {
                // 未登录,添加到Cookie
                // 5 将当前购物车结果集保存Cookie
                Cookie cookie = new Cookie("cart", JSON.toJSONString(carts));
                cookie.setMaxAge(60*60*24*7);
                cookie.setPath("/");
                // 6 将Cookie响应到浏览器中
                response.addCookie(cookie);
            }
            return new Result(true, "添加购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加购物车失败");
        }
    }

    /**
     * 查询购物车结果集
     * @param request
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts = null;
        // 1 获取Cookie数组
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            // 2 获取购物车结果集
            if ("cart".equals(cookie.getName())) {
                carts = JSON.parseArray(cookie.getValue(), Cart.class);
                break;
            }
        }
        // 判断用户是否登录
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(username)) {
            // 已登录,从缓存中取出购物车结果集
            if (null != carts && carts.size() > 0) {
                // 合并缓存和Cookie中的购物车结果集
                cartService.mergeCart(carts, username);
                // 清空Cookie数据
                Cookie cookie = new Cookie("cart", null);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            carts = cartService.selectCartsFromRedis(username);
        }
        if (null != carts && carts.size() > 0) {
            // 3 结果集不为空,则填充结果集(无论是cookie还是缓存中的购物车结果集,都是只存有部分数据的集合)
            carts = cartService.fillCarts(carts);
        }
        // 4 返回
        return carts;
    }
}