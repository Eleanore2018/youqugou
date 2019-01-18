package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.user.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    void sendCode(String phone);

    void add(String smscode, User user);

    PageResult searchUser(Integer currentPage, Integer itemsPerPage);

    void freezeUser(Long[] ids);

    List<Map> userCount();

    List<Integer> userActive();

    void addselfCenter(User user);

    User loginInfo(String username);
}
