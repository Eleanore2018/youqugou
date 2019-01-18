package cn.itcast.core.controller;
/**
 * 马超  2019.1.2
 */
import cn.itcast.common.util.FastDFSClient;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserPlus;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

@RestController
@RequestMapping("/self")
public class selfCenterController {

    @Value("${FILE_SERVER_URL}")
    private String url;

    @Reference
    private UserService userService;

//    @RequestMapping("/loginInfo")
//    public Map loginInfo() {
//        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Map<String, String> map = new HashMap<>();
//        map.put("username", user.getUsername());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String loginTime = format.format(new Date());
//        map.put("loginTime", loginTime);
//        return map;
//    }

    // 回显
    @RequestMapping("/loginInfo")
    public Map<String, String> loginInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("username", SecurityContextHolder.getContext().getAuthentication().getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String loginTime = format.format(new Date());
        map.put("loginTime", loginTime);
        return map;
    }

        // 个人中心数据更新
    @RequestMapping("/selfCenter")
    public Result addselfCenter(@RequestBody UserPlus userPlus){
        try {
            String username =  SecurityContextHolder.getContext().getAuthentication().getName();
            // 获取id
            User user1=userService.loginInfo(username);
            Long id = user1.getId();
            // 个人信息
            User user = userPlus.getUser();
            user.setId(id);
            user.setUsername(username);
            //生日
            String year = userPlus.getYear();
            String month = userPlus.getMonth();
            String day = userPlus.getDay();
            String str=year+"-"+month+"-"+day;
            if (year!=null&&month!=null&&day!=null){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date=format.parse(str);
                user.setBirthday(date);
            }

            userService.addselfCenter(user);

            return new Result(true,"修改成功");
        } catch (Exception e) {
            return new Result(false,"修改失败");
        }
    }

    // 图片上传
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) {
        try {
            // 使用工具类
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
            // apache自带的文件名工具类获取扩展名
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());

            // 上传文件
            String filePath = fastDFSClient.uploadFile(file.getBytes(), ext);
            //保存到数据库
            String path=url + filePath;
            return new Result(true,url + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "文件上传失败");
        }
    }
}
