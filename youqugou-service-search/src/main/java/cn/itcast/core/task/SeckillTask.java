package cn.itcast.core.task;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SeckillTask {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    //每天24点执行查询秒杀商品表，将符合条件的记录并且缓存中不存在的秒杀商品存入缓存  张静 2018-12-31
    //@Scheduled(cron = "59 59 23 * * ?")
    @Scheduled(cron = "* * * * * ?")
    public void refreshSeckillGoods() {
        //查询所有的秒杀商品键集合
        List ids = new ArrayList(redisTemplate.boundHashOps("seckillGoods").keys());
        //查询当天秒杀的商品列表      
        SeckillGoodsQuery example = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");//审核通过
        criteria.andStockCountGreaterThan(0);//剩余库存大于0
        criteria.andStartTimeGreaterThanOrEqualTo(new Date());//开始时间大于等于当前时间
        criteria.andEndTimeLessThanOrEqualTo(new Date(new Date().getTime()+24*60*60*1000));//结束时间是当天时间
        /*if (null!=ids&&ids.size()>0){
            criteria.andIdNotIn(ids);//排除缓存中已经有的商品
        }*/
        List<SeckillGoods> seckillGoodsList = seckillGoodsDao.selectByExample(example);
        //装入缓存
        for (SeckillGoods seckill : seckillGoodsList) {
            redisTemplate.boundHashOps("seckillGoods").put(seckill.getId(), seckill);
            //设置过期时间 24小时之后
            redisTemplate.expire(seckill.getId(), 24,TimeUnit.HOURS);
        }
    }


    //每秒中在缓存的秒杀上皮列表中查询过期的商品，发现过期同步到数据库，并在缓存中移除该秒杀商品 张静 2018-12-31
    @Scheduled(cron = "0 * * * * ?")
    public void removeSeckilGoods() {
        //扫描缓存中秒杀商品列表，发现过期的移除
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();
        for (SeckillGoods seckill : seckillGoodsList) {
            if (seckill.getEndTime().getTime() < new Date().getTime()){
                //如果结束日期小于当前日期，则表示过期
                seckillGoodsDao.updateByPrimaryKey(seckill);//向数据库保存记录
                redisTemplate.boundHashOps("seckillGoods").delete(seckill.getId());//移除缓存数据
            }
        }
    }
}
