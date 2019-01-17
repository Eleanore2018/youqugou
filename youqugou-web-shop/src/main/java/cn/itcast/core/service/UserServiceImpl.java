package cn.itcast.core.service;

import cn.itcast.core.pojo.seller.Seller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

// 由于扫描使用的是dubbo,所以不能使用spring的注解扫描,只能在配置文件注入对象
public class UserServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Seller seller = sellerService.selectSellerById(username);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (null != seller) {
            if ("1".equals(seller.getStatus())) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
                return new User(username, seller.getPassword(), grantedAuthorities);
            }
        }
        return null;
    }
}
