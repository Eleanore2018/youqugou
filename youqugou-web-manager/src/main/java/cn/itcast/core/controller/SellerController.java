package cn.itcast.core.controller;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    @RequestMapping("/searchSeller")
    public PageResult searchSeller(Integer pageNum, Integer pageSize, @RequestBody Seller seller) {
        return sellerService.searchSeller(pageNum, pageSize, seller);
    }

    @RequestMapping("/selectSellerById")
    public Seller selectSellerById(String sellerId) {
        return sellerService.selectSellerById(sellerId);
    }
}
