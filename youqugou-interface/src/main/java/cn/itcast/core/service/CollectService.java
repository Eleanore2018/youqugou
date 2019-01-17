package cn.itcast.core.service;

import cn.itcast.core.pojo.Collect;

import java.util.List;

/**
 * @左建洲
 *
 */
public interface CollectService {
    List<Collect> select(String username);
}
