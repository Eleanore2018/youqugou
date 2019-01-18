package cn.itcast.core.service;
/**
 * 马超  2019.1.2
 */
import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.pojo.address.*;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class addressServiceImpl implements AddressService{

    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Address> findListByLoginUser(String username) {
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.createCriteria().andUserIdEqualTo(username);
        return addressDao.selectByExample(addressQuery);
    }

    @Override
    public void addressService(Address address) {

        addressDao.insert(address);
    }

    @Override
    public void deleteAddress(Long id) {
        addressDao.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Address address) {
        addressDao.updateByPrimaryKeySelective(address);
    }

    @Override
    public Address findOne(Long id) {
        return addressDao.selectByPrimaryKey(id);
    }

}
