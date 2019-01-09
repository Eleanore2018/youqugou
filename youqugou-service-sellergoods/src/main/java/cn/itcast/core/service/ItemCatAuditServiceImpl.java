package cn.itcast.core.service;


import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贾运通 2018/12/31
 */
@Service
@Transactional
public class ItemCatAuditServiceImpl implements ItemCatAuditService {

    @Autowired
    private ItemCatDao itemCatDao;


    @Override
    public List<ItemCat> selectItemCatsByParentId(Long parentId) {
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
        return itemCatDao.selectByExample(itemCatQuery);
    }


    @Override
    public PageResult searchItemCatAudit(Integer pageNum, Integer pageSize, ItemCat itemcat) {
        PageHelper.startPage(pageNum, pageSize);

        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andStatusEqualTo("0");//只查询未审核的
        ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();
        if (null != itemcat.getName() && !"".equals(itemcat.getName().trim())) {
            criteria.andNameLike(itemcat.getName().trim());
        }
        if (null != itemcat.getTypeId()) {
            criteria.andTypeIdEqualTo(itemcat.getTypeId());
        }
        // 根据父id查询分类集合
        if (null != itemcat.getParentId()) {
            criteria.andParentIdEqualTo(itemcat.getParentId());
        }
        Page<ItemCat> page = (Page<ItemCat>) itemCatDao.selectByExample(itemCatQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }


    //修改状态
    @Override
    public void updateStatus(Long[] ids, String status) {
        ItemCat itemCat = new ItemCat();
        itemCat.setStatus(status);
        for (Long id : ids) {
            itemCat.setId(id);
            itemCatDao.updateByPrimaryKeySelective(itemCat);
        }
    }




    @Override
    public void insertItemCat(ItemCat itemCat) {
        itemCatDao.insertSelective(itemCat);
    }

    @Override
    public void updateItemCat(ItemCat itemCat) {
        itemCatDao.updateByPrimaryKeySelective(itemCat);
    }

    @Override
    public List<ItemCat> selectAllItemCats() {
        return itemCatDao.selectByExample(null);
    }

    @Override
    public List<Map> selectItemCatsMapByParentId(Long parentId) {
        List<Map> maps = itemCatDao.selectItemCatsMapByParentId(parentId);
        // 手动添加一个id为0的map项表示不选
        Map map = new HashMap();
        map.put("id", 0);
        map.put("text", "不选择");
        maps.add(map);
        return maps;
    }

    @Override
    public ItemCat selectItemCatById(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }
}