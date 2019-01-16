package cn.itcast.core.service;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.user.User;

import java.util.List;

public interface SellerService {
    void insertSeller(Seller seller);

    PageResult searchSeller(Integer pageNum, Integer pageSize, Seller seller);

    Seller selectSellerById(String sellerId);

    List<Seller> selectAllSellers();
}
