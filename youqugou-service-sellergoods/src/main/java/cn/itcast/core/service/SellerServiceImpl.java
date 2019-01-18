package cn.itcast.core.service;

import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerDao sellerDao;

    @Override
    public void insertSeller(Seller seller) {
        //未审核的商品
        //seller.setStatus("0");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        seller.setPassword(bCryptPasswordEncoder.encode(seller.getPassword()));
        // 状态值：  0：未审核   1：已审核   2：审核未通过   3：关闭
        seller.setStatus("0");
        //商品 注册 的时间
        seller.setCreateTime(new Date());

        sellerDao.insertSelective(seller);
    }

    @Override
    public PageResult searchSeller(Integer pageNum, Integer pageSize, Seller seller) {
        PageHelper.startPage(pageNum, pageSize);
        SellerQuery sellerQuery = new SellerQuery();
        SellerQuery.Criteria criteria = sellerQuery.createCriteria();
        if (null != seller.getName() && !"".equals(seller.getName().trim())) {
            criteria.andNameLike("%" + seller.getName().trim() + "%");
        }
        if (null != seller.getNickName() && !"".equals(seller.getNickName().trim())) {
            criteria.andNickNameLike("%" + seller.getNickName().trim() + "%");
        }
        if (null != seller.getStatus() && !"".equals(seller.getStatus())) {
            criteria.andStatusEqualTo(seller.getStatus());
        }
        Page<Seller> page = (Page<Seller>) sellerDao.selectByExample(sellerQuery);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Seller selectSellerById(String sellerId) {
        return sellerDao.selectByPrimaryKey(sellerId);
    }

    @Override
    public List<Seller> selectAllSellers() {
        return sellerDao.selectByExample(null);
    }
}
