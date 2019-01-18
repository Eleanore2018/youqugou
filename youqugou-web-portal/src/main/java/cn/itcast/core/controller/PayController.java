package cn.itcast.core.controller;

import cn.itcast.core.entity.Result;
import cn.itcast.core.service.PayService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private PayService payService;

    @RequestMapping("/createNative")
    public Map<String, String> createNative() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return payService.createNative(username);
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        try {
            int x = 0;
            while (true) {
                Map<String, String> map = payService.queryPayStatus(out_trade_no);
                //判断交易状态
                if ("SUCCESS".equals(map.get("trade_state"))) {
//                SUCCESS—支付成功
                    System.out.println(map.get("trade_state"));
                    System.out.println();
                    return new Result(true, "支付成功");
                }
                Thread.sleep(3000);
                x++;
                if (x > 100) {
                    //五分钟
                    //调用微信那边关闭订单Api (同学完成了)
                    return new Result(false, "二维码超时");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "支付失败");
        }
    }
}
