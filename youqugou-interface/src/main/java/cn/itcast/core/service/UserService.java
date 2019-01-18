package cn.itcast.core.service;

import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;

import java.util.List;

public interface UserService {
    void sendCode(String phone);

    void add(String smscode, User user);

    void addselfCenter(User user);

    User loginInfo(String username);

}
