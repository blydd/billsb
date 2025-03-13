package com.bgt.billsb.dao;

import com.bgt.billsb.entity.Bill;
import com.bgt.billsb.vo.BillDetail;
import com.bgt.billsb.vo.BillTypeVo;
import com.bgt.billsb.vo.PayTypeVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDao {
    /**
     * 查询全部账单
     * @return
     */
    public List<BillDetail> getAllBills() {
        final String SQL = "SELECT a.id,a.money,a.bill_time,a.create_time,a.desc,b.bill_type,b.icon,c.pay_type,c.pay_account_name,a.inout FROM t_bill a \n" +
                "left join t_bill_type b on a.bill_type_id=b.id \n" +
                "left join t_pay_type c on a.pay_type_id = c.id\n" +
                "order by a.bill_time desc";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            List<BillDetail> users = new ArrayList<>();
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                users.add(new BillDetail(
                    rs.getInt("id"),
                    rs.getDouble("money"),
                    rs.getString("bill_time"),
                    rs.getString("create_time"),
                    rs.getString("desc"),
                    rs.getString("bill_type"),
                    rs.getString("icon"),
                    rs.getString("pay_type"),
                    rs.getInt("inout"),
                    rs.getString("pay_account_name")
                ));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Query failed", e);
        }
    }

    /**
     * 查询全部账单类型
     * @return
     */
    public List<BillTypeVo> getBillTypes() {
        final String SQL = "SELECT * from t_bill_type";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            List<BillTypeVo> users = new ArrayList<>();
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(new BillTypeVo(
                        rs.getInt("id"),
                        rs.getString("bill_type"),
                        rs.getString("icon"),
                        rs.getInt("sort")

                ));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Query failed", e);
        }
    }

    public List<PayTypeVo> getPayTypes() {
        final String SQL = "SELECT * from t_pay_type";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            List<PayTypeVo> users = new ArrayList<>();
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(new PayTypeVo(
                        rs.getInt("id"),
                        rs.getString("pay_type"),
                        rs.getString("pay_account_code"),
                        rs.getString("pay_account_name")

                ));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Query failed", e);
        }
    }

    /**
     * 新保存账单
     * @param newBill
     */
    public void addBill(Bill newBill) {
        //组装插入sql
        final String SQL = "INSERT INTO t_bill (money, bill_time, create_time, bill_type_id, pay_type_id,inout, desc) VALUES (?, ?, ?, ?, ?,?, ?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setDouble(1, newBill.getMoney());
            pstmt.setString(2, newBill.getBillTime());
            pstmt.setString(3, newBill.getCreateTime());
            pstmt.setInt(4, newBill.getBillTypeId());
            pstmt.setInt(5, newBill.getPayTypeId());
            pstmt.setInt(6, newBill.getInout());
            pstmt.setString(7, newBill.getDesc());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Insert failed", e);
        }
    }

    public void deleteBill(Integer id) {
        final String SQL = "DELETE FROM t_bill WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Delete failed", e);
        }
    }
}
