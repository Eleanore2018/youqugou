package cn.itcast.core.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 张静 2018-12-28  显示用户名
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/loginInfo")
    public Map loginInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, String> map = new HashMap<>();
        map.put("username", user.getUsername());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String loginTime = format.format(new Date());
        map.put("loginTime", loginTime);
        return map;
    }
}