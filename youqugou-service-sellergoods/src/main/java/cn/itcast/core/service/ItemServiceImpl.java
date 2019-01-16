package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;
    @Override
    public PageResult searchitemPage(Integer pageNum, Integer pageSize, Item item) {
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("id desc");
        ItemQuery itemQuery = new ItemQuery();
     //   ItemQuery.Criteria criteria = itemQuery.createCriteria();
//        if (null != item.getGoodsName() && !"".equals(goods.getGoodsName().trim())) {
//            criteria.andGoodsNameLike(goods.getGoodsName().trim());
//        }
//        if (null != goods.getAuditStatus() && !"".equals(goods.getAuditStatus().trim())) {
//            criteria.andAuditStatusEqualTo(goods.getAuditStatus());
//        }
        //criteria.andIsDeleteIsNull();
        Page<Item> page = (Page<Item>) itemDao.selectByExample(itemQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
