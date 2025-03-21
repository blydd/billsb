package com.bgt.billsb.dao;

import cn.hutool.core.util.StrUtil;
import com.bgt.billsb.dto.BillDto;
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
import java.util.Objects;

public class BillDao {
    /**
     * 查询全部账单
     * @return
     */
    public List<BillDetail> getAllBills(BillDto param) {
        final String SQL = "SELECT a.id,a.money,a.bill_time,a.create_time,a.desc,b.bill_type,b.icon,c.pay_type,c.pay_account_name,a.inout FROM t_bill a \n" +
                "left join t_bill_type b on a.bill_type_id=b.id \n" +
                "left join t_pay_type c on a.pay_type_id = c.id\n" +
                getQueryParamSql(param) +
                " order by a.bill_time desc";
        
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
            e.printStackTrace();
            throw new RuntimeException("Query failed", e);
        }
    }

    /**
     * 拼接查询条件
     * @param param
     * @return
     */
    private String getQueryParamSql(BillDto param) {
        String sql = "";
        if (Objects.nonNull(param)) {
            sql += " where 1=1";
            //账单月份
            if (StrUtil.isNotBlank(param.getBillMonth())) {
                sql += " and a.bill_time like '"+param.getBillMonth()+"%'";
            }
            //支出收入不计入
            if (Objects.nonNull(param.getInout())){
                sql += " and a.inout="+param.getInout();
            }
            //账单类型
            if (StrUtil.isNotBlank(param.getBillType())) {
                sql += " and b.bill_type='" + param.getBillType() + "'";
            }
            //支付方式
            if (StrUtil.isNotBlank(param.getPayType())) {
                sql += " and c.pay_account_name='" + param.getPayType() + "'";
            }
            //账单id
            if (Objects.nonNull(param.getId())){
                sql += " and a.id="+param.getId();
            }
            return sql;
        }else {
            return sql;
        }
    }

    /**
     * 查询全部账单类型
     * @param type 收入/支出
     * @return
     */
    public List<BillTypeVo> getBillTypes(String type) {
        String SQL = "SELECT * from t_bill_type";
        if (StrUtil.isNotBlank(type)){
            SQL += " where type='"+type+"'";
        }

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
        String SQL = "INSERT INTO t_bill (money, bill_time, create_time, bill_type_id, pay_type_id,inout, desc) VALUES (?, ?, ?, ?, ?,?, ?)";
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
    /**
     * 新保存账单
     * @param updatedBill
     */
    public void updateBill(Bill updatedBill) {
        //组装插入sql
        final String SQL =
                "UPDATE t_bill SET money = ?, bill_time = ?, create_time = ?, bill_type_id = ?, pay_type_id = ?, inout = ?, `desc` = ? " +
                        "WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setDouble(1, updatedBill.getMoney());
            pstmt.setString(2, updatedBill.getBillTime());
            pstmt.setString(3, updatedBill.getCreateTime());
            pstmt.setInt(4, updatedBill.getBillTypeId());
            pstmt.setInt(5, updatedBill.getPayTypeId());
            pstmt.setInt(6, updatedBill.getInout());
            pstmt.setString(7, updatedBill.getDesc());
            pstmt.setInt(8, updatedBill.getId()); // 确保Bill对象中有id字段
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update failed", e);
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
