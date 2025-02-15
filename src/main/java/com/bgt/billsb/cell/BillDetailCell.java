package com.bgt.billsb.cell;

import com.bgt.billsb.vo.BillDetail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

/**
 * @author: bgt
 * @Date: 2025/2/5 14:52
 * @Desc: 每日账单列表详情
 */
public class BillDetailCell extends ListCell<BillDetail> {

    private List<BillDetail> datas;


    public BillDetailCell(List<BillDetail> datas) {
        this.datas = datas;
    }


    @Override
    protected void updateItem(BillDetail billDetail, boolean empty) {
        super.updateItem(billDetail, empty);
        if (empty || billDetail == null) {
            setGraphic(null);
        } else {
            try {
                /*每日账单明细列表*/
                    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/com/bgt/billsb/billListDetailView.fxml"));
                    BorderPane billView = loader2.load();
                    //每日账单明细列表塞值
                    Label billType = (Label) billView.lookup("#billType");
                    Label billTimeAndDesc = (Label) billView.lookup("#billTimeAndDesc");
                    Label billMoney = (Label) billView.lookup("#billMoney");
                    billType.setText(billDetail.getBillType());
                    billTimeAndDesc.setText(billDetail.getBillTime().concat("  ").concat(billDetail.getDesc()));
                    billMoney.setText(String.valueOf(billDetail.getMoney()));

                    setGraphic(billView);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
