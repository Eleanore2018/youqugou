package cn.itcast.core.service;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemQuery;

import cn.itcast.core.pojogroup.GoodsVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination topicPageAndSolrDestination;
    @Autowired
    private Destination queueSolrDeleteDestination;

    @Override
    public void insertGoodsVo(GoodsVo goodsVo) {
        // 插入商品时,初始状态都为0,即未审核
        goodsVo.getGoods().setAuditStatus("0");
        goodsDao.insertSelective(goodsVo.getGoods());
        // 商品表中的id与商品详情表的goodId为同一id
        goodsVo.getGoodsDesc().setGoodsId(goodsVo.getGoods().getId());
        goodsDescDao.insertSelective(goodsVo.getGoodsDesc());

        // 判断是否启用规格 1为启动,0或null皆为不启用
        if ("1".equals(goodsVo.getGoods().getIsEnableSpec())) {
            List<Item> itemList = goodsVo.getItemList();
            itemList.forEach(item -> {
                // title标题 由名称加规格组成
                String title = goodsVo.getGoods().getGoodsName();
                // 获取json字符串格式的规格并解析成map集合
                Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    title += " " + entry.getValue();
                }
                item.setTitle(title);
                // 取第一张图片作为显示图片
                String itemImages = goodsVo.getGoodsDesc().getItemImages();
                List<Map> list = JSON.parseArray(itemImages, Map.class);
                item.setImage((String) list.get(0).get("url"));

                // 设置类目id及名称
                Long category3Id = goodsVo.getGoods().getCategory3Id();
                item.setCategoryid(category3Id);
                ItemCat itemCat = itemCatDao.selectByPrimaryKey(category3Id);
                item.setCategory(itemCat.getName());

                // 设置创建时间和修改时间
                item.setCreateTime(new Date());
                item.setUpdateTime(new Date());

                // 设置商品id
                item.setGoodsId(goodsVo.getGoods().getId());

                // 设置品牌名称
                item.setBrand(brandDao.selectByPrimaryKey(goodsVo.getGoods().getBrandId()).getName());

                // 设置公司名及id
                String sellerId = goodsVo.getGoods().getSellerId();
                item.setSellerId(sellerId);
                item.setSeller(sellerDao.selectByPrimaryKey(sellerId).getName());

                // 保存item
                itemDao.insertSelective(item);
            });
        }
    }

    @Override
    public PageResult searchGoodsPage(Integer pageNum, Integer pageSize, Goods goods) {
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("id desc");
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        if (null != goods.getGoodsName() && !"".equals(goods.getGoodsName().trim())) {
            criteria.andGoodsNameLike(goods.getGoodsName().trim());
        }
        if (null != goods.getAuditStatus() && !"".equals(goods.getAuditStatus().trim())) {
            criteria.andAuditStatusEqualTo(goods.getAuditStatus());
        }
        criteria.andIsDeleteIsNull();
        //如果sellerid不为空,表示查询商户自己的数据,否则是运营商查询,查询所有商品数据  张静 2018-12-30
        if (goods.getSellerId() != null) {
            criteria.andSellerIdEqualTo(goods.getSellerId());
        }
        Page<Goods> page = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public GoodsVo selectGoodsVoById(Long id) {
        GoodsVo goodsVo = new GoodsVo();
        Goods goods = goodsDao.selectByPrimaryKey(id);
        goodsVo.setGoods(goods);

        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        goodsVo.setGoodsDesc(goodsDesc);

        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        List<Item> items = itemDao.selectByExample(itemQuery);
        goodsVo.setItemList(items);

        return goodsVo;
    }

    @Override
    public void updateGoodsVo(GoodsVo goodsVo) {
        goodsDao.updateByPrimaryKeySelective(goodsVo.getGoods());
        goodsDescDao.updateByPrimaryKeySelective(goodsVo.getGoodsDesc());

        // 先删除
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goodsVo.getGoods().getId());
        itemDao.deleteByExample(itemQuery);

        // 判断是否启用规格 1为启动,0或null皆为不启用
        if ("1".equals(goodsVo.getGoods().getIsEnableSpec())) {
            List<Item> itemList = goodsVo.getItemList();
            itemList.forEach(item -> {
                // title标题 由名称加规格组成
                String title = goodsVo.getGoods().getGoodsName();
                // 获取json字符串格式的规格并解析成map集合
                Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    title += " " + entry.getValue();
                }
                item.setTitle(title);
                // 取第一张图片作为显示图片
                String itemImages = goodsVo.getGoodsDesc().getItemImages();
                List<Map> list = JSON.parseArray(itemImages, Map.class);
                item.setImage((String) list.get(0).get("url"));

                // 设置类目id及名称
                Long category3Id = goodsVo.getGoods().getCategory3Id();
                item.setCategoryid(category3Id);
                ItemCat itemCat = itemCatDao.selectByPrimaryKey(category3Id);
                item.setCategory(itemCat.getName());

                // 设置创建时间和修改时间
                item.setCreateTime(new Date());
                item.setUpdateTime(new Date());

                // 设置商品id
                item.setGoodsId(goodsVo.getGoods().getId());

                // 设置品牌名称
                item.setBrand(brandDao.selectByPrimaryKey(goodsVo.getGoods().getBrandId()).getName());

                // 设置公司名及id
                String sellerId = goodsVo.getGoods().getSellerId();
                item.setSellerId(sellerId);
                item.setSeller(sellerDao.selectByPrimaryKey(sellerId).getName());

                // 保存item
                itemDao.insertSelective(item);
            });
        }
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        Goods goods = new Goods();
        goods.setAuditStatus(status);
        for (Long id : ids) {
            goods.setId(id);
            goodsDao.updateByPrimaryKeySelective(goods);

            if ("1".equals(status)) {
                // 将审核通过的商品添加到索引库和生成静态页,属于MQ中的主题模式
                jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(String.valueOf(id));
                    }
                });
            }
        }
    }

    @Override
    public void delete(Long[] ids) {
        Goods goods = new Goods();
        goods.setIsDelete("1");
        for (Long id : ids) {
            goods.setId(id);
            goodsDao.updateByPrimaryKeySelective(goods);
            // 删除时更新索引库
            jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(String.valueOf(id));
                }
            });
        }
    }
}