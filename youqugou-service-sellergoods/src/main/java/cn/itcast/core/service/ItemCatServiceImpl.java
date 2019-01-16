package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ItemCat> selectItemCatsByParentId(Long parentId) {
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
        return itemCatDao.selectByExample(itemCatQuery);
    }

    @Override
    public ItemCat selectItemCatById(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateItemCat(ItemCat itemCat) {
        ItemCat cat = itemCatDao.selectByPrimaryKey(itemCat.getId());
        redisTemplate.boundHashOps("itemCat").delete(cat.getName());
        redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
        itemCatDao.updateByPrimaryKeySelective(itemCat);
    }

    @Override
    public PageResult searchItemCat(Integer pageNum, Integer pageSize, ItemCat itemcat) {
        // 将所有的分类名称与typeId之间的对应存到缓存库
        List<ItemCat> itemCats = selectAllItemCats();
        if (redisTemplate.boundHashOps("itemCat").values() == null || redisTemplate.boundHashOps("itemCat").values().size() <= 0) {
            for (ItemCat itemCat : itemCats) {
                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
            }
        }

        PageHelper.startPage(pageNum, pageSize);
        ItemCatQuery itemCatQuery = new ItemCatQuery();
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

    @Override
    public void insertItemCat(ItemCat itemCat) {
        redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
        itemCatDao.insertSelective(itemCat);
    }

    @Override
    public List<ItemCat> selectAllItemCats() {
        return itemCatDao.selectByExample(null);
    }

    /**
     * 王浩宇修改2018/12/29
     * @param parentId
     * @return
     */
    //----------------------------
    @Override
    public List<Map> selectItemCatsMapByParentId(Long parentId) {
//        List<Map> maps = itemCatDao.selectItemCatsMapByParentId(parentId);
//        // 手动添加一个id为0的map项表示不选
//        Map map = new HashMap();
//        map.put("id", 0);
//        map.put("text", "不选择");
//        maps.add(map);
        return null;
    }
    //-------------------------------
}
