package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojogroup.OrderItemVo;
import cn.itcast.core.service.OrderService;
import cn.itcast.core.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单管理 张静 2018-12-30
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    /*查询所有订单 2018-12-30 张静*/
    @RequestMapping("/searchOrder")
    public PageResult searchOrder(Integer pageNum, Integer pageSize, @RequestBody Order order) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(name);
        return orderService.searchOrder(pageNum, pageSize, order);
    }

    /*查询所有订单 张静 2018-12-30*/
    @RequestMapping("/selectOrderById")
    public List<OrderItem> selectOrderById(Long id) {
        return orderService.selectOrderById(id);
    }

    //查询指定id的订单详情 张静  2018-12-30
    @RequestMapping("/selectOrderItemById")
    public OrderItemVo selectOrderItemById(Long id) {
        return orderService.selectOrderItemById(id);
    }

    //更新指定id的订单(发货) 张静 2018-12-30
    @RequestMapping("/updateOrderById")
    public Result updateOrderById(@RequestBody Order order) {
        try {
            orderService.updateOrderById(order);
            return new Result(true, "发货成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发货失败!");
        }
    }

    /**
     * Author: Mr Liu
     * Date: 2019/1/2 00:26
     * use to get category and sales by sellerId
     */
    @RequestMapping("/getSalesByCategory")
    public List<Map<String, Object>> getSalesByCategory(Integer year, Integer month, String timeBucket) throws ParseException {
        // 获取当前登录商家
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 按照-规则切割时间段,获取到一头一尾的数据
        String[] split = timeBucket.split("-");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 开始时间
        Date startDate = format.parse("" + year + "-" + month + "-" + split[0]);
        // 结束时间
        Date endDate = format.parse("" + year + "-" + month + "-" + split[1]);
        return orderService.getSalesByCategory(sellerId, startDate, endDate);
    }
    @RequestMapping("/downloadOrder")
    public String downloadOrder(HttpServletRequest request, HttpServletResponse response) {
        List<Order> orderList = orderService.selectAllOrder();
        String sheetName = "订单列表";
        String headers[] = {"订单号", "实付金额", "支付类型", "邮费", "状态", "订单创建时间", "订单更新时间",
                "付款时间", "发货时间", "交易完成时间", "交易关闭时间", "物流名称", "物流单号",
                 "买家留言", "买家昵称", "买家是否已经评价", "收货人地区名称(省，市，县)街道",
                "收货人手机", "收货人邮编", "收货人", "过期时间，定期清理", "发票类型(普通发票，电子发票，增值税发票)",
                "订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端"};
        String exportExcelName = "订单列表";
        String fansTypeFileName = "订单";
        XSSFWorkbook workbook = exportExcel(sheetName, orderList, headers, fansTypeFileName);

        OutputStream outputStream = null;
        try {
            String fileName = new String(exportExcelName.getBytes(),"ISO8859-1");
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName+".xls");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    /**
     *  导出Excel
     * @param sheetName 表格 sheet 的名称
     * @param headers  标题名称
     * @param dataList 需要显示的数据集合
     */
    public  static XSSFWorkbook exportExcel(String sheetName, List<Order> dataList,
                                            String[] headers, String fansTypeFileName) {

        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(30);

        // 生成表格中非标题栏的样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);//背景色
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // 生成表格中非标题栏的字体
        XSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        // 把字体应用到当前的样式
        style.setFont(font);


        // 设置表格标题栏的样式
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置标题栏字体
        XSSFFont titleFont = workbook.createFont();
        titleFont.setColor(HSSFColor.WHITE.index);
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setBold(true);
        // 把字体应用到当前的样式
        titleStyle.setFont(titleFont);

        //设置普通样式
        XSSFCellStyle normalStyle = workbook.createCellStyle();
        // 设置这些样式
        normalStyle.setFillForegroundColor(HSSFColor.WHITE.index);//背景色
        normalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        normalStyle.setAlignment(HorizontalAlignment.CENTER);
        // 生成表格中非标题栏的字体
        XSSFFont normalFont = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        // 把字体应用到当前的样式
        normalStyle.setFont(font);

        sheet.addMergedRegion(new CellRangeAddress(0,1,0,23));
        XSSFRow row1 = sheet.createRow(0);
        XSSFCell cell8 = row1.createCell(0);
        cell8.setCellStyle(style);
        XSSFRichTextString textString = new XSSFRichTextString(fansTypeFileName+"列表");
        cell8.setCellValue(textString);
        // 产生表格标题行
        XSSFRow row = sheet.createRow(2);
        for (short i = 0; i < headers.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellStyle(titleStyle);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        int index = 2;
        for (Order order : dataList) {
            index++;
            row = sheet.createRow(index);

            //订单id
            XSSFCell cell = row.createCell(0);
            cell.setCellStyle(style);
            XSSFRichTextString text = new XSSFRichTextString(order.getOrderId().toString());
            cell.setCellValue(text);

            //实付金额
            XSSFCell cell1 = row.createCell(1);
            cell1.setCellStyle(style);
            BigDecimal payment = order.getPayment();
            if (payment == null){
                payment = new BigDecimal("0.0");
            }
            XSSFRichTextString text1 = new XSSFRichTextString(payment.toString());
            cell1.setCellValue(text1);

            //支付类型
            XSSFCell cell2 = row.createCell(2);
            cell2.setCellStyle(style);
            String paymentType = order.getPaymentType();
            if (paymentType == null){
                paymentType = "在线支付";
            }
            XSSFRichTextString text2 = new XSSFRichTextString(paymentType.toString());
            cell2.setCellValue(text2);

            //邮费
            XSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(style);
            String postFee = order.getPostFee();
            if (postFee == null){
                postFee = "0.0";
            }
            XSSFRichTextString text3 = new XSSFRichTextString(paymentType.toString());
            cell3.setCellValue(text3);

            //状态
            XSSFCell cell4 = row.createCell(4);
            cell4.setCellStyle(style);
            String status = order.getStatus();
            if (status == null){
                status = "未付款";
            }
            XSSFRichTextString text4 = new XSSFRichTextString(paymentType.toString());
            cell4.setCellValue(text4);

            //订单创建时间
            XSSFCell cell5 = row.createCell(5);
            cell5.setCellStyle(style);
            Date createTime = order.getCreateTime();
            if (createTime == null){
                createTime = new Date();
            }
            XSSFRichTextString text5 = new XSSFRichTextString(paymentType.toString());
            cell5.setCellValue(text5);

            //订单更新时间
            XSSFCell cell6 = row.createCell(6);
            cell6.setCellStyle(style);
            Date updateTime = order.getUpdateTime();
            if (updateTime == null){
                updateTime = new Date();
            }
            XSSFRichTextString text6 = new XSSFRichTextString(paymentType.toString());
            cell6.setCellValue(text6);

            //付款时间
            XSSFCell cell7 = row.createCell(7);
            cell7.setCellStyle(style);
            Date paymentTime = order.getPaymentTime();
            if (paymentTime == null){
                paymentTime = new Date();
            }
            XSSFRichTextString text7 = new XSSFRichTextString(paymentTime.toString());
            cell7.setCellValue(text7);

            //发货时间
            XSSFCell cell9 = row.createCell(8);
            cell9.setCellStyle(style);
            Date consignTime = order.getConsignTime();
            if (consignTime == null){
                consignTime = new Date();
            }
            XSSFRichTextString text8 = new XSSFRichTextString(consignTime.toString());
            cell9.setCellValue(text8);

            //交易完成时间
            XSSFCell cell10 = row.createCell(9);
            cell10.setCellStyle(style);
            Date endTime = order.getEndTime();
            if (endTime == null){
                endTime = new Date();
            }
            XSSFRichTextString text9 = new XSSFRichTextString(endTime.toString());
            cell10.setCellValue(text9);

            //交易关闭时间
            XSSFCell cell11 = row.createCell(10);
            cell11.setCellStyle(style);
            Date closeTime = order.getCloseTime();
            if (closeTime == null){
                closeTime = new Date();
            }
            XSSFRichTextString text10 = new XSSFRichTextString(closeTime.toString());
            cell11.setCellValue(text9);

            //交易关闭时间
            XSSFCell cell12 = row.createCell(11);
            cell12.setCellStyle(style);
            String shippingName = order.getShippingName();
            if (shippingName == null){
                shippingName = "品优购";
            }
            XSSFRichTextString text11 = new XSSFRichTextString(shippingName.toString());
            cell12.setCellValue(text11);

            //物流单号
            XSSFCell cell13 = row.createCell(12);
            cell13.setCellStyle(style);
            String shippingCode = order.getShippingCode();
            if (shippingCode == null){
                shippingCode = "666666";
            }
            XSSFRichTextString text12 = new XSSFRichTextString(shippingCode.toString());
            cell13.setCellValue(text12);

            //买家留言
            XSSFCell cell14 = row.createCell(13);
            cell14.setCellStyle(style);
            String buyerMessage = order.getBuyerMessage();
            if (buyerMessage == null){
                buyerMessage = "无";
            }
            XSSFRichTextString text13 = new XSSFRichTextString(buyerMessage.toString());
            cell14.setCellValue(text13);

            //买家昵称
            XSSFCell cell15 = row.createCell(14);
            cell15.setCellStyle(style);
            String buyerNick = order.getBuyerNick();
            if (buyerNick == null){
                buyerNick = "品优购用户";
            }
            XSSFRichTextString text14 = new XSSFRichTextString(buyerNick.toString());
            cell15.setCellValue(text14);

            //买家是否已经评价
            XSSFCell cell16 = row.createCell(15);
            cell16.setCellStyle(style);
            String buyerRate = order.getBuyerRate();
            if (buyerRate == null){
                buyerRate = "否";
            }
            XSSFRichTextString text15 = new XSSFRichTextString(buyerRate.toString());
            cell16.setCellValue(text15);

            //收货人地区
            XSSFCell cell17 = row.createCell(16);
            cell17.setCellStyle(style);
            String receiverAreaName = order.getReceiverAreaName();
            if (receiverAreaName == null){
                receiverAreaName = "大中华区";
            }
            XSSFRichTextString text16 = new XSSFRichTextString(receiverAreaName.toString());
            cell17.setCellValue(text16);

            //收货人手机
            XSSFCell cell18 = row.createCell(17);
            cell18.setCellStyle(style);
            String receiverMobile = order.getReceiverMobile();
            if (receiverMobile == null){
                receiverMobile = "13888888888";
            }
            XSSFRichTextString text17 = new XSSFRichTextString(receiverMobile.toString());
            cell18.setCellValue(text17);

            //收货人邮编
            XSSFCell cell19 = row.createCell(18);
            cell19.setCellStyle(style);
            String receiverZipCode = order.getReceiverZipCode();
            if (receiverZipCode == null){
                receiverZipCode = "100000";
            }
            XSSFRichTextString text18 = new XSSFRichTextString(receiverZipCode.toString());
            cell19.setCellValue(text18);

            //收货人
            XSSFCell cell20 = row.createCell(19);
            cell20.setCellStyle(style);
            String receiver = order.getReceiver();
            if (receiver == null) {
                receiver = "赵四";
            }
            XSSFRichTextString text19 = new XSSFRichTextString(receiver.toString());
            cell20.setCellValue(text19);

            //过期时间
            XSSFCell cell21 = row.createCell(20);
            cell21.setCellStyle(style);
            Date expire = order.getExpire();
            if (expire == null) {
                expire = new Date();
            }
            XSSFRichTextString text20 = new XSSFRichTextString(expire.toString());
            cell21.setCellValue(text20);

            //发票类型
            XSSFCell cell22 = row.createCell(21);
            cell22.setCellStyle(style);
            String invoiceType = order.getInvoiceType();
            if (invoiceType == null) {
                invoiceType = "无";
            }
            XSSFRichTextString text21 = new XSSFRichTextString(invoiceType.toString());
            cell22.setCellValue(text21);

            //订单来源
            XSSFCell cell23 = row.createCell(22);
            cell23.setCellStyle(style);
            String sourceType = order.getSourceType();
            if (sourceType == null) {
                sourceType = "PC端";
            }
            XSSFRichTextString text22 = new XSSFRichTextString(sourceType.toString());
            cell23.setCellValue(text22);
            //昵称
/*            XSSFCell cell2 = row.createCell(2);
            cell2.setCellStyle(style);
            XSSFRichTextString text2 = new XSSFRichTextString(member.getNickname());
            cell2.setCellValue(text2);
            //等级
            XSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(style);
            Integer level = member.getLevel();
            String levelStr = null;
            if (level == 0){
                levelStr = "达人";
            }else if (level == 1){
                levelStr = "小v";
            } else if (level == 2){
                levelStr = "大v";
            }
            XSSFRichTextString text3 = new XSSFRichTextString(levelStr);
            cell3.setCellValue(text3);
            //创建时间
            XSSFCell cell4 = row.createCell(4);
            cell4.setCellStyle(style);
            Date crTime = member.getCrTime();
            String dateFormart = DateFormatUtils.format(crTime, "yyyy-MM-dd");
            XSSFRichTextString text4 = new XSSFRichTextString(dateFormart);
            cell4.setCellValue(text4);
            //累计结算收益
            XSSFCell cell5 = row.createCell(5);
            cell5.setCellStyle(style);
            XSSFRichTextString text5 = new XSSFRichTextString(member.getCumulativeIncome()+"");
            cell5.setCellValue(text5);
            //账户余额
            XSSFCell cell6 = row.createCell(6);
            cell6.setCellStyle(style);
            XSSFRichTextString text6 = new XSSFRichTextString(member.getAccountBalance()+"");
            cell6.setCellValue(text6);
            //积分
            XSSFCell cell7 = row.createCell(7);
            cell7.setCellStyle(style);
            XSSFRichTextString text7 = new XSSFRichTextString(member.getIntegral()+"");
            cell7.setCellValue(text7);*/
        }
        XSSFRow sheetRow = sheet.createRow(index+2);
        XSSFCell timeCell = sheetRow.createCell(6);
        timeCell.setCellStyle(normalStyle);
        XSSFRichTextString timeText = new XSSFRichTextString("日期:");
        timeCell.setCellValue(timeText);

        XSSFCell timecell1 = sheetRow.createCell(7);
        timecell1.setCellStyle(normalStyle);
        XSSFRichTextString timeText1 = new XSSFRichTextString(DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        timecell1.setCellValue(timeText1);

        return workbook;
    }
}
