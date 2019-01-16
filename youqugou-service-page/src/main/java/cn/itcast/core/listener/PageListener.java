package cn.itcast.core.listener;

import cn.itcast.core.service.ItemStaticHtmlService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class PageListener implements MessageListener {
    @Autowired
    private ItemStaticHtmlService itemStaticHtmlService;

    @Override
    public void onMessage(Message message) {
        // 将审核通过的商品进行静态化处理生成对应的详情页
        TextMessage textMessage = (TextMessage)message;
        try {
            String id = textMessage.getText();
            itemStaticHtmlService.genItemDetailStaticHtml(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
