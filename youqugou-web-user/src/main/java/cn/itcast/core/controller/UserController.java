package cn.itcast.core.controller;
/**
 * 马超  2019.1.2
 */

import cn.itcast.common.util.PhoneFormatCheckUtils;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserPlus;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;

    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {
        try {
            if (PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                userService.sendCode(phone);
                return new Result(true, "成功");
            } else {
                return new Result(false, "手机号不合法");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "失败");
        }
    }




    @RequestMapping("/add")
    public Result add(String smscode, @RequestBody User user) {
        try {
            userService.add(smscode, user);
            return new Result(true, "注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败");
        }
    }
}
