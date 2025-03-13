package com.bgt.billsb.util;

import com.bgt.billsb.eenum.DataTypeEnum;
import com.bgt.billsb.dto.ResultEvent;
import com.bgt.billsb.service.BillService;
import com.bgt.billsb.service.impl.BillServiceImpl;
import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class DataUtil{
    //账单数据
//    public static ObservableList<BillDay> datas = FXCollections.observableArrayList();
//    public static ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();


    private static final BillService billService = new BillServiceImpl();

    /**
     * 异步加载数据
     */
//    public static void loadDataAsync() {
//        Service<List<BillDetail>> dataService = new Service<>() {
//            @Override
//            protected Task<List<BillDetail>> createTask() {
//                return new Task<>() {
//                    @Override
//                    protected List<BillDetail> call() {
//                        return billService.getAll();
//                    }
//                };
//            }
//        };
//
//        dataService.setOnSucceeded(e -> {
//            ObservableList<BillDay> newdatas = FXCollections.observableArrayList();
//            //查询完毕后把数据转成页面所需格式
//            List<BillDetail> value = dataService.getValue();
//
//            Map<String, List<BillDetail>> groupByBilltime = value.stream().collect(Collectors.groupingBy(a -> a.getBillTime().split(" ")[0]));
//            Iterator<Map.Entry<String, List<BillDetail>>> it = groupByBilltime.entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry<String, List<BillDetail>> next = it.next();
//                BillDay billDay = new BillDay();
//                billDay.setDate(next.getKey());
//                List<BillDetail> billDetails = next.getValue();
//                billDay.setBillDetailList(billDetails);
//
//                billDay.setDayTotalIn(billDetails.stream().filter(a -> a.getMoney() > 0D).mapToDouble(BillDetail::getMoney).sum());
//                billDay.setDayTotalOut(billDetails.stream().filter(a -> a.getMoney() < 0D).mapToDouble(BillDetail::getMoney).sum());
//
//                newdatas.add(billDay);
//            }
//            //查询完数据后发布到事件总线
////            datas.clear();
//            datas.addAll(newdatas);
//            EventBusUtil.getDefaut().post(new ResultEvent(200,"success",datas));
//            System.out.println("查询到数据的账单数量 = " + datas.size() +"   "+newdatas.size() +"   "+groupByBilltime.size());
//        }
//        );
//        dataService.start();
//    }

    /**
     * 加载账单数据
     */
    public static ObservableList<BillDay> queryData() {
            List<BillDetail> value = billService.getAll();

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
        EventBusUtil.getDefaut().post(new ResultEvent(DataTypeEnum.BILLDATA,newdatas));
            return newdatas;
        }


    /**
     * 加载账单类型数据
     */
    public static void getBillTypes() {
        EventBusUtil.getDefaut().post(new ResultEvent(DataTypeEnum.BILLTYPE, FXCollections.observableArrayList( billService.getBillTypes())));
    }

    /**
     * 加载支付类型数据
     */
    public static void getPayTypes() {
        EventBusUtil.getDefaut().post(new ResultEvent(DataTypeEnum.PAYTYPE, FXCollections.observableArrayList(billService.getPayTypes())));
    }



}