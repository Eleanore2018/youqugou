package cn.itcast.core.service;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.Areas;
import cn.itcast.core.pojo.address.Cities;
import cn.itcast.core.pojo.address.Provinces;

import java.util.List;

public interface AddressService {
    List<Address> findListByLoginUser(String username);

    void addressService(Address address);

    void deleteAddress(Long id);

    void update(Address address);

    Address findOne(Long id);

   /* List<Provinces> findList();

    List<Cities> findList2(String id);

    List<Areas> findList3(String id);*/
}
