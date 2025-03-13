package com.bgt.billsb.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: bgt
 * @Date: 2025/3/13 17:11
 * @Desc:
 */
public class ControllerManager {
    private ControllerManager(){}

    private static Map<String,Object> comtrollerMap = new HashMap<>();

    public static Object getControoler(String key){
        return comtrollerMap.get(key);
    }

    public static void setController(String key,Object value){
        comtrollerMap.put(key,value);
    }
}
