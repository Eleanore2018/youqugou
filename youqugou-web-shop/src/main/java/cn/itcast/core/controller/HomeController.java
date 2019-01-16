package cn.itcast.core.controller;

import cn.itcast.core.service.HomeService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author:Mr Liu
 * Date:2018-12-28
 */
@RestController
@RequestMapping("/home")
public class HomeController {
    @Reference
    private HomeService homeService;

    /**
     * 查询商家审核通过时的日期
     * Author:Mr Liu
     * Date:2018-12-29
     * @return
     */
    @RequestMapping("/searchTime")
    public List<Integer> searchTime() {
        // 获取当前登录商家
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        Date date = homeService.searchTime(sellerId);
        List<Integer> list = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        now.setTime(date);

        list.add(now.get(Calendar.YEAR));
        list.add(now.get(Calendar.MONTH) + 1);
        list.add(now.get(Calendar.DAY_OF_MONTH));
        return list;
    }

    /**
     * Author:Mr Liu
     * Date:2018-12-29
     */
    @RequestMapping("/getSales")
    public Map<String, List> getSales (Integer year, Integer month, String timeBucket) throws ParseException {
        // 按照-规则切割时间段,获取到一头一尾的数据
        String[] split = timeBucket.split("-");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> list = new ArrayList<>();
        // 获取当前登录的商家名
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        // 添加横轴数据(日期)
        List<Integer> horizontalAxis = new ArrayList<>();
        for (int i = Integer.parseInt(split[0]); i <= Integer.parseInt(split[1]); i++) {
            horizontalAxis.add(i);
            list.add(format.parse(year + "-" + month + "-" + i));
        }
        // 添加纵轴数据(每日销售额)
        List<Double> verticalAxis =  homeService.getSales(list, sellerId);
        Map<String, List> map = new HashMap<>();
        map.put("horizontalAxis", horizontalAxis);
        map.put("verticalAxis", verticalAxis);
        return map;
    }
}

