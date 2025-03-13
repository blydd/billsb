package com.bgt.billsb.util;

import com.google.common.eventbus.EventBus;

/**
 * @author: bgt
 * @Date: 2025/3/13 09:02
 * @Desc:
 */
public class EventBusUtil {
    //构造私有
    private EventBusUtil(){}

    private final static EventBus eventBus = new EventBus();

    public static EventBus getDefaut (){
        return eventBus;
    }

}
