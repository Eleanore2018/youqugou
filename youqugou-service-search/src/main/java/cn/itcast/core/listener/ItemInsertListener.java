package cn.itcast.core.listener;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.*;
import java.util.List;

public class ItemInsertListener implements MessageListener {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String id = textMessage.getText();
            ItemQuery itemQuery = new ItemQuery();
            itemQuery.createCriteria().andGoodsIdEqualTo(Long.parseLong(id)).andStatusEqualTo("1").andIsDefaultEqualTo("1");
            List<Item> items = itemDao.selectByExample(itemQuery);
            solrTemplate.saveBeans(items, 1000);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
