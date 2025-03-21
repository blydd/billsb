package com.bgt.billsb.util;

import com.bgt.billsb.dto.BillDto;
import com.bgt.billsb.eenum.DataTypeEnum;
import com.bgt.billsb.dto.ResultEvent;
import com.bgt.billsb.service.BillService;
import com.bgt.billsb.service.impl.BillServiceImpl;
import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillDetail;
import com.bgt.billsb.vo.BillTypeVo;
import com.bgt.billsb.vo.PayTypeVo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class DataUtil{
    public static DateTimeFormatter DTF_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    private static final BillService billService = new BillServiceImpl();

    /**
     * 如果字符小于指定长度,在前面补0
     */
    public static String addZero(String str, int len) {
        int strLen = str.length();
        if (strLen < len) {
            while (strLen < len) {
                str = "0" + str;
                strLen++;
            }
        }
        return str;
    }

    /**
     * 加载账单数据
     */
    public static ObservableList<BillDay> queryData(BillDto param) {
        //默认查询当前月份
        if (Objects.isNull(param)){
            param = new BillDto();
            param.setBillMonth(getCurrentMonth());
        }
            List<BillDetail> value = billService.getAll(param);
        if (Objects.nonNull(param.getId())) {
            EventBusUtil.getDefaut().post(new ResultEvent(DataTypeEnum.ONEBILL, value));
            return null;
        }

        ObservableList<BillDay> newdatas = FXCollections.observableArrayList();
            //查询完毕后把数据转成页面所需格式

            Map<String, List<BillDetail>> groupByBilltime = value.stream().collect(Collectors.groupingBy(a -> a.getBillTime().split(" ")[0]));
            Iterator<Map.Entry<String, List<BillDetail>>> it = groupByBilltime.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<BillDetail>> next = it.next();
                BillDay billDay = new BillDay();
                billDay.setDate(next.getKey());
                List<BillDetail> billDetails = next.getValue();
                billDay.setBillDetailList(billDetails);

                billDay.setDayTotalIn(billDetails.stream().filter(a -> a.getMoney() > 0D).mapToDouble(BillDetail::getMoney).sum());
                billDay.setDayTotalOut(billDetails.stream().filter(a -> a.getMoney() < 0D).mapToDouble(BillDetail::getMoney).sum());

                newdatas.add(billDay);
            }
            //按日期倒序
        newdatas = FXCollections.observableArrayList(newdatas.stream()
                .filter(Objects::nonNull) // 过滤掉无效的对象
                .sorted(Comparator.comparing(BillDay::getDate).reversed()) // 按解析后的日期排序
                .toList());
            //发送消息通知
            EventBusUtil.getDefaut().post(new ResultEvent(DataTypeEnum.BILLDATA,newdatas));
            return newdatas;
        }


    /**
     * 加载账单类型数据 例如 衣食住行
     * @param type 收入/支出
     */
    public static List<BillTypeVo> getBillTypes(String type,boolean ifPushEvent) {
        List<BillTypeVo> billTypes = billService.getBillTypes(type);
        if (ifPushEvent){

        EventBusUtil.getDefaut().post(new ResultEvent(DataTypeEnum.BILLTYPE, FXCollections.observableArrayList(billTypes)));
        return null;
        }else {

        return billTypes;
        }
    }

    /**
     * 加载支付类型数据 例如微信  支付宝
     */
    public static List<PayTypeVo> getPayTypes(boolean ifPushEvent) {
        List<PayTypeVo> payTypes = billService.getPayTypes();
        if (ifPushEvent){
        EventBusUtil.getDefaut().post(new ResultEvent(DataTypeEnum.PAYTYPE, FXCollections.observableArrayList(payTypes)));
            return null;
        }else {

        return payTypes;
        }
    }


    public static void deleteBill(Integer id) {
        billService.deleteBill(id);
    }

    /**
     * 获取当前月份 yyyy-MM
     * @return
     */
    public static String getCurrentMonth() {
        LocalDate now = LocalDate.now();
        String format = DTF_MONTH.format(now);

        return format;
    }
}