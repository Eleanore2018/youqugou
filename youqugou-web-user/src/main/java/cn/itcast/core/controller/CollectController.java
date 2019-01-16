package cn.itcast.core.controller;

import cn.itcast.core.pojo.Collect;
import cn.itcast.core.service.CollectService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 *@zuojianzhou
 *
 */
@RestController
@RequestMapping("collect")
public class CollectController {

    @Reference
    private CollectService collectService;

    //查询我的收藏
    @RequestMapping("selectCollect")
    public List<Collect> selectColl(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousName".equals(username) && null != username){
            List<Collect> collectList = collectService.select(username);
            return collectList;
        }
        return  null;
    }
}
