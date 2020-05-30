package com.deepthink.common.web;

import java.io.Serializable;

/**
 * http请求响应结果
* @Description:
* @Param:
* @return:
* @Author: td
* @Date: 2019/11/27
*/
public class RemoteResult<T> implements Serializable {

	private static final long serialVersionUID = 7051462028989660320L;
	// 响应状态码：200成功，非200为失败
	private String code;
	// 响应信息：成功时是ok，失败时是错误描述
	private String msg;
	// 系统时间戳
	private long timestamp;
	private String errorCode;
	private T data;
	private static final int SUCCESS_CODE = 200;

	public RemoteResult() {
		code = String.valueOf(SUCCESS_CODE);
		msg = "ok";
		timestamp = System.currentTimeMillis();
	}

	public RemoteResult(T obj) {
		code = String.valueOf(SUCCESS_CODE);
		msg = "ok";
		timestamp = System.currentTimeMillis();
		data = obj;
	}

	public RemoteResult(String errorCode, String errrMsg) {
		code = errorCode;
		msg = errrMsg;
		timestamp = System.currentTimeMillis();
	}

	public RemoteResult(int errorCode, String errrMsg) {
		code = String.valueOf(errorCode);
		msg = errrMsg;
		timestamp = System.currentTimeMillis();
	}

	public RemoteResult(T obj, int capType, String productId) {
		code = String.valueOf(SUCCESS_CODE);
		msg = "ok";
		timestamp = System.currentTimeMillis();
		data = obj;
	}

	public RemoteResult(String code, String msg, T data) {
		this.code = code != null ? code : String.valueOf(SUCCESS_CODE);
		this.msg = msg;
		this.data = data;
		timestamp = System.currentTimeMillis();
	}


	public int getCode() {
		try {
			return Integer.valueOf(code);
		} catch (Exception e) {
			return 0;
		}
	}

	public String getCodeStr() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public T getData() {
		return this.data;
	}

	public void setData(T tx) {
		this.data = tx;
	}

	public void setCode(int code) {
		this.code = String.valueOf(code);
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isSuccess() {
		return String.valueOf(SUCCESS_CODE).equals(code);
	}

}
