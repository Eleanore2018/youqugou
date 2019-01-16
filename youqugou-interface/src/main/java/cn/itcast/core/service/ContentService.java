package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.ad.Content;

import java.util.List;
import java.util.Map;

public interface ContentService {

    public List<Content> findAll();

    public PageResult findPage(Content content, Integer pageNum, Integer pageSize);

    public void add(Content content);

    public void edit(Content content);

    public Content findOne(Long id);

    public void delAll(Long[] ids);

    List<Content> selectContentsByCategoryId(Long categoryId);

    Map<String,Map> findFloorContent(Long[] contentCategory);
}

