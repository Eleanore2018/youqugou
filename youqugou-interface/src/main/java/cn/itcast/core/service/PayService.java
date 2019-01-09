package cn.itcast.core.service;

import java.text.ParseException;
import java.util.Map;

public interface PayService {
    Map<String,String> createNative(String username);

    Map<String,String> queryPayStatus(String out_trade_no);

    void updatePayLog(String out_trade_no, String transaction_id, String time_end) throws ParseException;

    Map<String,String> closePay(String out_trade_no);

    void updateOrderStatus(String out_trade_no);
}
