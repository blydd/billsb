package com.bgt.billsb.dto;


import com.bgt.billsb.eenum.DataTypeEnum;

/**
 * @author: bgt
 * @Date: 2025/3/13 09:16
 * @Desc: 事件通知类型
 */
public class ResultEvent {
    /**
     * 数据类型
     */
    private DataTypeEnum dataType;
    /**
     * 数据
     */
    private Object data;

    public ResultEvent(DataTypeEnum dataType, Object data) {
        this.dataType = dataType;
        this.data = data;
    }


    public DataTypeEnum getDataType() {
        return dataType;
    }

    public Object getData() {
        return data;
    }
}
