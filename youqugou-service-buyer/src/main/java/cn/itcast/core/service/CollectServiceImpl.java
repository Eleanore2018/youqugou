package cn.itcast.core.service;

import cn.itcast.core.dao.collect.CollectDao;
import cn.itcast.core.pojo.Collect;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
/*
* 左建洲
* */
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectDao collectDao;


    @Override
    public List<Collect> select(String username) {

        List<Collect> collectList =  collectDao.select(username);

        return collectList;
    }
}
