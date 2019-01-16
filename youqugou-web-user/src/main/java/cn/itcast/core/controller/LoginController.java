package cn.itcast.core.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/name")
    public Map showUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousName".equals(username)) {
            Map map = new HashMap();
            map.put("loginName", username);
            return map;
        }
        return null;
    }
}

