package com.bgt.billsb.util;

import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillDetail;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: bgt
 * @Date: 2025/2/6 17:50
 * @Desc:
 */
public class TestUtil {
    /**
     * 测试数据
     */
    public static List<BillDay> getDatas() {
        List<BillDay> datas = new ArrayList<>();
        List<String> list = Arrays.asList("保险", "教育", "衣", "食", "住", "行");
        for (int i = 0; i < 10; i++) {
            BillDay billDay = new BillDay();
            billDay.setDate("2月" + (i + 1) + "日");

            billDay.setBillDetailList(new ArrayList<BillDetail>());

            for (int j = 0; j < 5; j++) {
                BillDetail billDetail = new BillDetail();
                //类型随机设置为保险\教育\衣\食\住\行中的一个

                billDetail.setBillType(list.get(new Random().nextInt(list.size())));
                billDetail.setDesc("打卡机点开链接" + j);
                billDetail.setTime("08:20");

                billDetail.setMoney(j * 100D);
                billDay.getBillDetailList().add(billDetail);
            }

            billDay.setDayTotalIn(billDay.getBillDetailList().stream().mapToDouble(BillDetail::getMoney).filter(d -> d > 0).sum());
            billDay.setDayTotalOut(billDay.getBillDetailList().stream().mapToDouble(BillDetail::getMoney).filter(d -> d < 0).sum());


            datas.add(billDay);
            datas = datas.stream().sorted(Comparator.comparingLong(d -> d.getDate().hashCode())).collect(Collectors.toList());
            Collections.reverse(datas);
        }
        return datas;
    }
}
