package com.deepthink.common.web;


/**
 * @Description : 异常码枚举
 * @Copyright : Sinaif Software Co.,Ltd. All Rights Reserved
 * @Company : deepthink.ai
 * @author : td
 * @version : 1.0
 * @Date : 2018年5月3日 下午2:04:08
 */
public enum ApiErrorEnum {
	
	SUCCESS(0, "ok"),
	
	WITHDRAW_APPLYING(200, "成功"),

	system_error(500, "系统错误"),

	parameter_error(500, "参数错误！"),

	op_deny(500, "操作禁止！"),

	user_not_exist(500, "用户不存在！"),

	COMMON_SERVICE_ERROR(10002, "服务异常"),
	;

	private int code;
	
	private String message;
	
	private ApiErrorEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	
	public String formatMsg(Object... msgs) {
		return String.format(message, msgs);
	}
}
