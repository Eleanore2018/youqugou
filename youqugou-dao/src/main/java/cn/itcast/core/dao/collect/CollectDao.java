package cn.itcast.core.dao.collect;

import cn.itcast.core.pojo.Collect;

import java.util.List;

public interface CollectDao {
    int insertSelective(Collect collect);

    List<Collect> select(String username);
}
