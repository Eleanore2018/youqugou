package cn.itcast.core.service;


import cn.itcast.common.util.IdWorker;
import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.AddressQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    //张静 2018-12-31
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Autowired
    private AddressDao addressDao;

    //张静 2018-12-31 查询秒杀商品
    @Override
    public List<SeckillGoods> findSeckillList() {
        //查询当前系统时间之前添加的,并没有结束的秒杀商品
        List<SeckillGoods> seckillGoods = redisTemplate.opsForHash().values("seckillGoods");
        return seckillGoods;
    }


    //张静 查询指定的秒杀商品 2018-12-31
    @Override
    public SeckillGoods findOne(Long id) {
         return (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(id);
    }


    @Autowired
    private IdWorker idWorker;

    //张静 实现商品秒杀 2019-01-01
    @Override
    public boolean goToSeckill(String name, Long id) {
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(id);
        //判断是否售完
        if (seckillGoods.getStockCount()>0){
            SeckillOrder seckillOrder=new SeckillOrder();
            //设置订单那状态1,未支付
            seckillOrder.setStatus("1");
            //商品id
            seckillOrder.setSeckillId(seckillGoods.getId());
            //总计
            seckillOrder.setMoney(seckillGoods.getCostPrice());
            //用户
            seckillOrder.setUserId(name);
            //id
            seckillOrder.setId(idWorker.nextId());
            seckillOrder.setSellerId(seckillGoods.getSellerId());
            seckillOrder.setCreateTime(new Date());
            //默认地址
            AddressQuery query=new AddressQuery();
            query.createCriteria().andUserIdEqualTo(name).andIsDefaultEqualTo("1");
            List<Address> addresses = addressDao.selectByExample(query);
            seckillOrder.setReceiver(addresses.get(0).getContact());
            seckillOrder.setReceiverAddress(addresses.get(0).getAddress());
            seckillOrder.setReceiverMobile(addresses.get(0).getMobile());
            seckillOrderDao.insertSelective(seckillOrder);
            seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
            redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getId(),seckillGoods);
            return true;
        }else{
            return false;
        }
    }
}
