package cn.itcast.core.service;

import cn.itcast.core.pojo.item.Item;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.*;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> searchItem(Map<String, String> searchMap) {
        // 定义搜索对象的结构  category:商品分类
        // $scope.searchMap={'keywords':'','category':'','brand':'',
        // 'spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};

        // 根据关键词keywords查询结果集(索引库查询)
        // 查询总条数total
        // 查询总页码totalPages
        searchMap.put("keywords", searchMap.get("keywords").replaceAll(" ", ""));
        Map<String, Object> map = getResult(searchMap);

        // 根据结果集查询分类列表(根据分类分组查询)
        List<String> categoryList = getCategoryList(searchMap);

        // 根据结果集查询分类列表
        map.put("categoryList", categoryList);

        // 根据分类列表第一个关联查询品牌列表(从缓存库查)brandList
        if (categoryList != null && categoryList.size() > 0) {
            // 根据分类列表的第一个数值名称查找类型id值
            Object typeId = redisTemplate.boundHashOps("itemCat").get(categoryList.get(0));

            // 根据类型id值查找brandList
            List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
            map.put("brandList", brandList);

            // 根据类型id值查找specList
            List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList", specList);
        }
        return map;
    }

    public List<String> getCategoryList(Map<String, String> searchMap) {
        // 根据关键词查询
        Query query = new SimpleQuery(new Criteria("item_keywords").is(searchMap.get("keywords")));
        GroupOptions groupOptions = new GroupOptions();
        // 分组项中添加需要分组的域名(字段)
        groupOptions.addGroupByField("item_category");
        // 将分组后的对象设置到查询对象中
        query.setGroupOptions(groupOptions);

        // 根据分组查询,参数需要一个query对象和操作的对象类型.class
        GroupPage<Item> items = solrTemplate.queryForGroupPage(query, Item.class);
        // 根据字段名获取结果集
        GroupResult<Item> itemCategory = items.getGroupResult("item_category");
        // 结果集中封装了多条信息,取出分组集合
        Page<GroupEntry<Item>> groupEntries = itemCategory.getGroupEntries();

        List<String> categoryList = new ArrayList<>();
        for (GroupEntry<Item> groupEntry : groupEntries) {
            // 将分组的value值添加进list集合中
            categoryList.add(groupEntry.getGroupValue());
        }
        return categoryList;
    }

    public Map<String, Object> getResult(Map<String, String> searchMap) {
        // 定义搜索对象的结构  category:商品分类
        // $scope.searchMap={'keywords':'','category':'','brand':'',
        // 'spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};

        HighlightQuery highlightQuery = new SimpleHighlightQuery();
        // 设置查询条件,根据item_keywords查询结果集
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        highlightQuery.addCriteria(criteria);
        // 根据分类查询
        if (searchMap.get("category") != null && !"".equals(searchMap.get("category").trim())) {
            highlightQuery.addCriteria(new Criteria("item_category").is(searchMap.get("category")));
        }
        // 根据品牌查询
        if (searchMap.get("brand") != null && !"".equals(searchMap.get("brand").trim())) {
            highlightQuery.addCriteria(new Criteria("item_brand").is(searchMap.get("brand")));
        }
        // 根据价格区间查询
        if (searchMap.get("price") != null && !"".equals(searchMap.get("price").trim())) {
            String[] prices = searchMap.get("price").split("-");
            if ("*".equals(prices[1])) {
                highlightQuery.addCriteria(new Criteria("item_price").greaterThan(prices[0]));
            } else {
                highlightQuery.addCriteria(new Criteria("item_price").between(prices[0], prices[1]));
            }
        }
        // 根据规格查询
        if (searchMap.get("spec") != null && !"".equals(searchMap.get("spec").trim())) {
            Map<String, String> specMap = JSON.parseObject(searchMap.get("spec"), Map.class);
            Set<Map.Entry<String, String>> entries = specMap.entrySet();
            if (null != entries && entries.size() > 0) {
                for (Map.Entry<String, String> entry : entries) {
                    highlightQuery.addCriteria(new Criteria("item_spec_" + entry.getKey()).is(entry.getValue()));
                }
            }
        }

        // 排序方式
        if (null != searchMap.get("sortField") && !"".equals(searchMap.get("sortField"))) {
            Sort sort;
            if ("DESC".equals(searchMap.get("sort"))) {
                sort = new Sort(Sort.Direction.DESC, "item_" + searchMap.get("sortField"));
            } else {
                sort = new Sort(Sort.Direction.ASC, "item_" + searchMap.get("sortField"));
            }
            highlightQuery.addSort(sort);
        }

        // 设置偏移量
        highlightQuery.setOffset((Integer.parseInt(searchMap.get("pageNo")) - 1) * Integer.parseInt(searchMap.get("pageSize")));
        // 设置每页数
        highlightQuery.setRows(Integer.parseInt(searchMap.get("pageSize")));

        // 设置关键词高亮
        HighlightOptions highlightOptions = new HighlightOptions();
        // 设置高亮字段
        highlightOptions.addField("item_title");
        // 设置高亮关键词前后缀
        highlightOptions.setSimplePrefix("<span style='color:red;'>");
        highlightOptions.setSimplePostfix("</span>");

        highlightQuery.setHighlightOptions(highlightOptions);
        HighlightPage<Item> page = solrTemplate.queryForHighlightPage(highlightQuery, Item.class);
        // 要注意docs和高亮域highlighting是分开的,要将高亮域的高亮片段设置到docs中
        List<HighlightEntry<Item>> items = page.getHighlighted();
        if (items != null && items.size() > 0) {
            for (HighlightEntry<Item> highlightEntry : items) {
                Item item = highlightEntry.getEntity();
                List<HighlightEntry.Highlight> highlights = highlightEntry.getHighlights();
                if (null != highlights && highlights.size() > 0) {
                    item.setTitle(highlights.get(0).getSnipplets().get(0));
                }
            }

            // 这里使用lambda表达式会出错
            /*items.forEach(itemHighlightEntry -> {
                Item item = itemHighlightEntry.getEntity();
                List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
                item.setTitle(highlights.get(0).getSnipplets().get(0));
            });*/
        }
        Map<String, Object> map = new HashMap<>();
        // 根据关键词keywords查询结果集(索引库查询)
        map.put("rows", page.getContent());
        // 查询总页码totalPages
        map.put("totalPages", page.getTotalPages());
        // 查询总条数total
        map.put("total", page.getTotalElements());
        return map;
    }
}