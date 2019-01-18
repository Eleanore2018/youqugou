package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userManager")
public class UserManagerController {

    @Reference
    private UserService userService;

    /**
     * @autuhor 陈伟鑫
     * @Date 12.31
     * @param currentPage
     * @param itemsPerPage
     * @return
     */
    @RequestMapping("/searchUser")
    public PageResult searchUser(Integer currentPage,Integer itemsPerPage) {
        return userService.searchUser(currentPage,itemsPerPage);
    }

    /**
     * @autuhor 陈伟鑫
     * @Date 12.31
     * 冻结用户操作
     * @param ids
     * @return
     */
    @RequestMapping("/freezeUser")
    public Result freezeUser(@RequestBody Long[] ids) {
        try {
            userService.freezeUser(ids);
            return new Result(true,"冻结用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"冻结用户失败");
        }
    }

    /**
     * @autuhor 陈伟鑫
     * @Date 12.31
     * 时时统计用户数量
     * @return
     */
    @RequestMapping("/userCount")
    public List<Map> userCount() {
        return userService.userCount();
    }


    /**
     * @autuhor 陈伟鑫
     * @Date 1.2
     * 统计用户活跃度
     * @return
     */
    @RequestMapping("/userActive")
    public List<Integer> userActive() {
        return userService.userActive();
    }
}
