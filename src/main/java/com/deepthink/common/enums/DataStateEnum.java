package com.deepthink.common.enums;

/**
 * @program: vending_admin
 * @description: 数据状态枚举
 * @author: td
 * @create: 2020-03-25 14:28
 **/
public enum DataStateEnum {
    VALID("valid"),

    INVALID("invalid"),
    ;

    public String state;

    DataStateEnum(String state) {
        this.state = state;
    }



}
