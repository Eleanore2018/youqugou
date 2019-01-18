package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.*;
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


    /**
     * @autuhor 陈伟鑫
     * @Date 12.31
     * @param currentPage
     * @param itemsPerPage
     * @return
     */
    @Override
    public PageResult searchUser(Integer currentPage, Integer itemsPerPage) {
        PageHelper.startPage(currentPage,itemsPerPage);
        Page<User> page = (Page<User>) userDao.selectByExample(null);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * @autuhor 陈伟鑫
     * @Date 12.31
     * @param ids
     */
    @Override
    public void freezeUser(Long[] ids) {
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));
        List<User> userList = userDao.selectByExample(userQuery);
        for (User user : userList) {
            user.setStatus("N");
            userDao.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * @autuhor 陈伟鑫
     * @Date 12.31
     * 时时统计用户数量
     * @return
     */
    @Override
    public List<Map> userCount() {
        List<Map> mapList = new ArrayList<>();
        //查询正常使用的用户
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andStatusEqualTo("Y");
        List<User> userList = userDao.selectByExample(userQuery);
        Map<String,Object> map = new HashMap<>();
        map.put("value",userList.size());
        map.put("name","正常用户");
        mapList.add(map);

        //查询被冻结用户
        UserQuery userQuery1 = new UserQuery();
        UserQuery.Criteria criteria1 = userQuery1.createCriteria();
        criteria1.andStatusEqualTo("N");
        List<User> userList1 = userDao.selectByExample(userQuery1);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("value",userList1.size());
        map1.put("name","冻结用户");
        mapList.add(map1);
        return mapList;
    }


    /**
     * @autuhor 陈伟鑫
     * @Date 1.2
     * 统计用户活跃度
     * @return
     */
    @Override
    public List<Integer> userActive() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(0);
        List<User> userList = userDao.selectByExample(null);

        for (User user : userList) {
            if (user.getLoginNu()>=0 && user.getLoginNu()<=100) {
                list.set(0,list.get(0)+1);
            } else if (user.getLoginNu()>100 && user.getLoginNu()<=200) {
                list.set(1,list.get(1)+1);
            } else if (user.getLoginNu()>200 && user.getLoginNu()<=300) {
                list.set(2,list.get(2)+1);
            } else if (user.getLoginNu()>300 && user.getLoginNu()<=400) {
                list.set(3,list.get(3)+1);
            } else if (user.getLoginNu()>400 && user.getLoginNu()<=500) {
                list.set(4,list.get(4)+1);
            }
        }
        return list;
    }
}
