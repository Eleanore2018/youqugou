package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author:Mr Liu
 * Date:2018-12-28
 */
@Service
@Transactional
public class HomeServiceImpl implements HomeService {
    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 查询商家审核通过时的日期
     * @return
     */
    @Override
    public Date searchTime(String sellerId) {
        return sellerDao.selectByPrimaryKey(sellerId).getCreateTime();
    }

    @Override
    public List<Integer> getSales(List<Date> list, String sellerId) {
        List<Integer> verticalAxis = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        list.forEach(date -> {
            try {
                OrderQuery orderQuery = new OrderQuery();
                orderQuery.createCriteria().andCreateTimeBetween(date, format.parse(format1.format(date) + " 23:59:59")).andSellerIdEqualTo(sellerId);
                verticalAxis.add(orderDao.countByExample(orderQuery));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return verticalAxis;
    }
}
