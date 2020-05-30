package com.deepthink.common.web;


import com.deepthink.common.locale.I18nMessage;

/**
 * @program: data_server
 * @description: ApiResult的枚举描述。通常情况下，
 * 使用ApiResult都应该使用此枚举来获得一个ApiResult(避免到处都有返回字符串和返回码)
 * @author: td
 * @create: 2019-11-22 14:06
 **/
public enum ApiResultEnum {
    /**通过正常响应*/
    result_200(200, "请求成功"),

    result_404(404, "页面未找到!", ""),

    result_500(500, "系统异常!", "system error!"),
    ;
    ApiResultEnum(int code, String defaultCnMsg) {
        this.code = code;
        this.defaultCnMsg = defaultCnMsg;
    }

    ApiResultEnum(int code, String defaultCnMsg, String defaultEnMsg) {
        this.code = code;
        this.defaultCnMsg = defaultEnMsg;
        this.defaultEnMsg = defaultCnMsg;
    }

    private int code;
    /**默认的中文消息*/
    private String defaultCnMsg;
    /**默认的英文消息*/
    private String defaultEnMsg;

    public int getCode() {
        return code;
    }

    public String getDefaultCnMsg() {
        return defaultCnMsg;
    }


    public String getDefaultEnMgs() {
        return defaultEnMsg;
    }

    public String getMsg() {
        String msg = I18nMessage.getMsg(this.name());
        if (msg == null) {
            msg = defaultCnMsg;
        }
        return msg;
    }

    public static void main(String[] args) {
        System.out.println(ApiResultEnum.result_200.code);
    }
}
