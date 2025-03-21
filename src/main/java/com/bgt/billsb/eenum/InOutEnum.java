package com.bgt.billsb.eenum;

/**
 * @author: bgt
 * @Date: 2025/3/13 16:07
 * @Desc:
 */
public enum InOutEnum {
    IN(1,"支出"),
    OUT(2,"收入"),
    bNOT(3,"不计入");

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    InOutEnum(int i, String name) {
        this.id = i;
        this.name = name;
    }

    //根据id获取名字
    public static String getName(Integer id) {
        for (InOutEnum c : InOutEnum.values()) {
            if (c.getId() == id) {
                return c.name;
            }
        }
        return null;
    }

    public static Integer getId(String name) {
        for (InOutEnum c : InOutEnum.values()) {
            if (c.getName().equals(name)) {
                return c.id;
            }
        }
        return null;
    }
}
