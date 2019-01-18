package cn.itcast.core.service;


import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemserviceImpl implements Itemservice{

    @Autowired
    private ItemDao itemDao;
    //张静 2019-01-02
    @Override
    public List<Item> findItemMapByGoodsId(Long id) {
        ItemQuery query=new ItemQuery();
        query.createCriteria().andGoodsIdEqualTo(id);
        return itemDao.selectByExample(query);
    }
}
