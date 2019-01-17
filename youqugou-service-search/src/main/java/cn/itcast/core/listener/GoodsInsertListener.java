package cn.itcast.core.listener;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

/**
 * @监听器
 * @作者:左建洲
 * @负责存储存储上架商品到索引库
 *
 * */
public class GoodsInsertListener implements MessageListener {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private SolrTemplate solrTemplate;


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage)message;
        try {
            String id = textMessage.getText();
            GoodsQuery goodsQuery = new GoodsQuery();
            goodsQuery.createCriteria().andIdEqualTo(Long.parseLong(id)).andAuditStatusEqualTo("4");
            List<Goods> goods = goodsDao.selectByExample(goodsQuery);
            solrTemplate.saveBeans(goods,1000);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
