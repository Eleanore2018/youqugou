package cn.itcast.core.service;

import java.util.Date;
import java.util.List;

/**
 * Author:Mr Liu
 * Date:2018-12-28
 */
public interface HomeService {
    Date searchTime(String sellerId);

    List<Integer> getSales(List<Date> list, String sellerId);
}
