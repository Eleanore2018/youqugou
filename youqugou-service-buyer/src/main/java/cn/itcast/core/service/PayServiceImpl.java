package cn.itcast.core.service;

import cn.itcast.common.util.HttpClient;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;
    @Value("${partnerkey}")
    private String partnerkey;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PayLogDao payLogDao;

    @Autowired
    private OrderItemDao orderItemDao;
    @Override
    public Map<String, String> createNative(String username) {
        PayLog payLog = (PayLog) redisTemplate.boundHashOps("payLog").get(username);
        //总金额
        Map<String, String> param = new HashMap<>();
//        字段名	变量名	必填	类型	示例值	描述
//        公众账号ID	appid	是	String(32)	wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appId）
        param.put("appid", appid);
//        商户号	mch_id	是	String(32)	1230000109	微信支付分配的商户号
        param.put("mch_id", partner);
//        设备号	device_info	否	String(32)	013467007045764	自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
//        随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，长度要求在32位以内。推荐随机数生成算法
        param.put("nonce_str", WXPayUtil.generateNonceStr());

//        商品描述	body	是	String(128)	腾讯充值中心-QQ会员充值
        param.put("body", "只要0.01元哟");
//        商品简单描述，该字段请按照规范传递，具体请见参数规定
//
//        商品详情	detail	否	String(6000)	 	商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明”
//        附加数据	attach	否	String(127)	深圳分店	附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
//        商户订单号	out_trade_no	是	String(32)	20150806125346	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号
        param.put("out_trade_no", payLog.getOutTradeNo());
//        标价币种	fee_type	否	String(16)	CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型
//        标价金额	total_fee	是	Int	88	订单总金额，单位为分，详见支付金额
        //param.put("total_fee", String.valueOf(payLog.getTotalFee()));
        param.put("total_fee", "1");
//        终端IP	spbill_create_ip	是	String(16)	123.12.12.123	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        param.put("spbill_create_ip", "127.0.0.1");
//        交易起始时间	time_start	否	String(14)	20091225091010	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
//        交易结束时间	time_expire	否	String(14)	20091227091010
//        订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则
//
//        建议：最短失效时间间隔大于1分钟
//
//        订单优惠标记	goods_tag	否	String(32)	WXG	订单优惠标记，使用代金券或立减优惠功能时需要的参数，说明详见代金券或立减优惠
//        通知地址	notify_url	是	String(256)	http://www.weixin.qq.com/wxpay/pay.php	异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        param.put("notify_url", "www.itcast.cn");
//        交易类型	trade_type	是	String(16)	JSAPI
        param.put("trade_type", "NATIVE");

        try {
//        签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	通过签名算法计算得出的签名值，详见签名生成算法
//        签名类型	sign_type	否	String(32)	MD5	签名类型，默认为MD5，支持HMAC-SHA256和MD5。
            String xml = WXPayUtil.generateSignedXml(param, partnerkey);
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            //Java 发出Https请求
            HttpClient httpClient = new HttpClient(url);
            //设置 https
            httpClient.setHttps(true);
            //入参:
            httpClient.setXmlParam(xml);
            //post提交
            httpClient.post();
            //响应
            String result = httpClient.getContent();
            //将xml 转成Map
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            //支付订单号
            map.put("out_trade_no",payLog.getOutTradeNo());
            //总金额
            map.put("total_fee",String.valueOf(payLog.getTotalFee()));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {
        Map<String, String> param = new HashMap<>();
//        字段名	变量名	必填	类型	示例值	描述
//        公众账号ID	appid	是	String(32)	wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appId）
        param.put("appid", appid);
//        商户号	mch_id	是	String(32)	1230000109	微信支付分配的商户号
        param.put("mch_id", partner);
        //商户订单号
        param.put("out_trade_no", out_trade_no);
//        设备号	device_info	否	String(32)	013467007045764	自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
//        随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，长度要求在32位以内。推荐随机数生成算法
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
//        签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	通过签名算法计算得出的签名值，详见签名生成算法
//        签名类型	sign_type	否	String(32)	MD5	签名类型，默认为MD5，支持HMAC-SHA256和MD5。
            String xml = WXPayUtil.generateSignedXml(param, partnerkey);
            String url = "https://api.mch.weixin.qq.com/pay/orderquery";
            //Java 发出Https请求
            HttpClient httpClient = new HttpClient(url);
            //设置 https
            httpClient.setHttps(true);
            //入参:
            httpClient.setXmlParam(xml);
            //post提交
            httpClient.post();
            //响应
            String result = httpClient.getContent();
            System.out.println(result);
            //将xml 转成Map
            Map<String, String> map = WXPayUtil.xmlToMap(result);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //订单支付成功,更改数据库的订单的状态  张静
    @Override
    public void updatePayLog(String out_trade_no, String transaction_id, String time_end) throws ParseException {
        PayLog payLog = payLogDao.selectByPrimaryKey(out_trade_no);
        payLog.setOutTradeNo(out_trade_no);
        payLog.setTransactionId(transaction_id);
        payLog.setPayTime(stringToDate(time_end));
        payLog.setTradeState("1");
        payLogDao.updateByPrimaryKeySelective(payLog);
        String orderList = payLog.getOrderList();
        String[] split = orderList.split(", ");
        for (String s : split) {
            Long orderId = Long.parseLong(s);
            Order order = orderDao.selectByPrimaryKey(orderId);
            order.setPaymentTime(stringToDate(time_end));
            order.setStatus("3");//已支付
            order.setUpdateTime(new Date());
            orderDao.updateByPrimaryKeySelective(order);
            OrderItemQuery query=new OrderItemQuery();
            query.createCriteria().andOrderIdEqualTo(orderId);
            OrderItem orderItem=new OrderItem();
            orderItem.setStatus("3");  //已支付
            orderItemDao.updateByExampleSelective(orderItem,query);
        }
        //清楚缓存中的支付日志对象
        redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
    }

    //关闭订单
    @Override
    public Map<String, String> closePay(String out_trade_no) {
        Map<String,String> map=new HashMap<>();
        //公众账号ID	appid	是	String(32)	wx8888888888888888	微信分配的公众账号ID（企业号corpid即为此appId）
        map.put("appid", appid);
        // 商户号	mch_id	是	String(32)	1900000109	微信支付分配的商户号
        map.put("mch_id",partner);
        // 商户订单号	out_trade_no	是	String(32)	1217752501201407033233368018	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一
        map.put("out_trade_no",out_trade_no);
        // 随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算
        map.put("nonce_str",WXPayUtil.generateNonceStr());
        // 签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
        // 签名类型	sign_type	否	String(32)	HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
        try {
            String xml = WXPayUtil.generateSignedXml(map, partnerkey);
            //获取httpclient对象
            String url="https://api.mch.weixin.qq.com/pay/closeorder";
            HttpClient httpClient=new HttpClient(url);
            //设置https
            httpClient.setHttps(true);
            //设置参数
            httpClient.setXmlParam(xml);
            //发送post请求
            httpClient.post();
            //获取响应结果
            String content = httpClient.getContent();
            System.out.println(content);
            Map<String,String> result=WXPayUtil.xmlToMap(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //关闭订单更改数据库
    @Override
    public void updateOrderStatus(String out_trade_no) {
        PayLog payLog = payLogDao.selectByPrimaryKey(out_trade_no);
        String orderList = payLog.getOrderList();
        String[] split = orderList.split(", ");
        for (String s : split) {
            Long orderId = Long.parseLong(s);
            Order order = orderDao.selectByPrimaryKey(orderId);
            order.setCloseTime(new Date());
            orderDao.updateByPrimaryKeySelective(order);
        }
    }


    //字符串转日期 张静
    private Date stringToDate(String time_end) throws ParseException {
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String year = time_end.substring(0, 4);
        String month = time_end.substring(4, 6);
        String day = time_end.substring(6,8);
        String hour = time_end.substring(8, 10);
        String mm = time_end.substring(10, 12);
        String ss = time_end.substring(12);
        return format.parse(year + "-" + month + "-" + day + " " + hour + ":" + mm + ":" + ss);
    }
}
