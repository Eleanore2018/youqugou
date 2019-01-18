package cn.itcast.core.controller;
/**
 * 马超  2019.1.2
 */
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.Areas;
import cn.itcast.core.pojo.address.Cities;
import cn.itcast.core.pojo.address.Provinces;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.AddressService;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
@RequestMapping("/address")
public class addressController {

    @Reference
    private AddressService addressService;

    @Reference
    private UserService userService;

    // 回显
    @RequestMapping("/loginInfo")
    public List<Address> loginInfo() {
        //通过登录人的username查询user
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();

        User user1=userService.loginInfo(username);

        List<Address> addressList = addressService.findListByLoginUser(user1.getUsername());

        return addressList;
    }

    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address){
        //通过登录人的username查询user
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        User user1=userService.loginInfo(username);
        try {
            address.setUserId(user1.getUsername());
            addressService.addressService(address);
            return new Result(true,"新增成功");
        } catch (Exception e) {
            return new Result(true,"新增失败");
        }
    }
    //添加
    @RequestMapping("/update")
    public Result update(@RequestBody Address address){
        try {
            addressService.update(address);
            return new Result(true,"成功");
        } catch (Exception e) {
            //e.printStackTrace();
            return new Result(false,"失败");
        }
    }


    @RequestMapping("/deleteAddress")
    public Result deleteAddress(@RequestBody Long id){

        try {
            addressService.deleteAddress(id);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            return new Result(true,"删除失败");

        }
    }
    //编辑地址回显
    @RequestMapping("/editBack")
    public Result editBack(@RequestBody Long id){

        try {
            addressService.deleteAddress(id);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            return new Result(true,"删除失败");

        }
    }

    @RequestMapping("/findOne")
    public Address findOne(Long id){
        return addressService.findOne(id);
    }

   /* @RequestMapping("/findList")
    public List<Provinces> findList(){
        return addressService.findList();
    }

    @RequestMapping("/findList2")
    public List<Cities> findList2(String id){
        return addressService.findList2(id);
    }

    @RequestMapping("/findList3")
    public List<Areas> findList3(String id){
        return addressService.findList3(id);
    }*/
}
