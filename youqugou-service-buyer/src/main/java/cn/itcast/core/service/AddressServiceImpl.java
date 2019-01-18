package cn.itcast.core.service;

import cn.itcast.core.dao.address.AddressDao;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AddressServiceImpl  {
    @Autowired
    private AddressDao addressDao;

//    @Override
//    public List<Address> findListByLoginUser(String username) {
//        AddressQuery addressQuery = new AddressQuery();
//        addressQuery.createCriteria().andUserIdEqualTo(username);
//        return addressDao.selectByExample(addressQuery);
//    }
}
