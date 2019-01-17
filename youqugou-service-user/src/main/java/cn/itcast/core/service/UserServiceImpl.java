package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination smsDestination;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserDao userDao;

    @Override
    public void sendCode(String phone) {
        String randomNumeric = RandomStringUtils.randomNumeric(6);
        redisTemplate.boundValueOps(phone).set(randomNumeric);
        redisTemplate.boundValueOps(phone).expire(5, TimeUnit.HOURS); // 实际应该是五分钟
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("phoneNumbers", phone);
                mapMessage.setString("templateParam", "{\"code\":" + randomNumeric + "}");
                mapMessage.setString("templateCode", "SMS_130916347");
                mapMessage.setString("signName", "橙子网络科技平台");
                return mapMessage;
            }
        });
    }

    @Override
    public void add(String smscode, User user) {
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        if (null == code) {
            throw new RuntimeException("验证码失效");
        } else {
            if (code.equals(smscode)) {
                // 验证码成功则注册成功
                user.setCreated(new Date());
                user.setUpdated(new Date());
                userDao.insertSelective(user);
            } else {
                throw new RuntimeException("验证码错误");
            }
        }
    }
}
