package cn.itcast.core.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @下架页面删除索引库
 * @作者:左建洲
 *
 * */
public class GoodsDeleteListener implements MessageListener{

    @Autowired
    private SolrTemplate solrTemplate;


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage)message;

        try {
            String id = textMessage.getText();
            SolrDataQuery solrDataQuery = new SimpleQuery(new Criteria("audit_status").is(id));
            solrTemplate.delete(solrDataQuery);
            solrTemplate.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
