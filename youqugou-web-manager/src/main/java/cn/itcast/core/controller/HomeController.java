package cn.itcast.core.controller;

import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.HomeService;
import cn.itcast.core.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author:Mr Liu
 * Date:2018-12-30
 */
@RestController
@RequestMapping("/home")
public class HomeController {
    @Reference
    private HomeService homeService;
    @Reference
    private SellerService sellerService;

    /**
     * Author:Mr Liu
     * Date:2018-12-29
     */
    @RequestMapping("/getSales")
    public Map<String, Map<String, List>> getSales (Integer year, Integer month, String timeBucket) throws ParseException {
        // 按照-规则切割时间段,获取到一头一尾的数据
        String[] split = timeBucket.split("-");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Map<String, List>> chart = new HashMap<>();
        Map<String, List> lineChart = new HashMap<>();
        List<Date> list = new ArrayList<>();

        // 定义一个map,储存所有商家数据及销量
        Map<String, List> cookieChart = new HashMap<>();
        // 定义一个集合,储存所有商家数据
        List<String> sellerNames = new ArrayList<>();
        // 定义一个集合,存储商家对应的销量
        List<Double> sellerSales = new ArrayList<>();

        // 添加横轴数据(日期)
        List<Integer> horizontalAxis = new ArrayList<>();
        for (int i = Integer.parseInt(split[0]); i <= Integer.parseInt(split[1]); i++) {
            horizontalAxis.add(i);
            list.add(format.parse(year + "-" + month + "-" + i));
        }
        lineChart.put("horizontalAxis", horizontalAxis);

        // 添加纵轴数据(每日销售额)
        List<Double> verticalAxis =  homeService.getSales(list);
        lineChart.put("verticalAxis", verticalAxis);
        chart.put("lineChart", lineChart);

        // 查找出所有商家
        List<Seller> sellers = sellerService.selectAllSellers();
        // 遍历商家查找出所有商家的日销售额
        for (Seller seller : sellers) {
            sellerNames.add(seller.getName());
            List<Double> sales = homeService.getSales(list, seller.getSellerId());
            // 求出销售的总和
            Double sum = sum(sales);
            sellerSales.add(sum);
        }
        cookieChart.put("sellerNames", sellerNames);
        cookieChart.put("sellerSales", sellerSales);
        chart.put("cookieChart", cookieChart);
        return chart;
    }

    private Double sum(List<Double> sales) {
        double sum = 0.0;
        for (Double sale : sales) {
            sum += sale;
        }
        return sum;
    }
}