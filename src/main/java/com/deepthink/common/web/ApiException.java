package com.deepthink.common.web;

import com.alibaba.fastjson.JSONObject;

/**
 * @author     : td
 * @version    : 1.0
 **/
public class ApiException extends RuntimeException {
	
	private static final long serialVersionUID = -855029911285953787L;
	private int error;
	private String message;
	private JSONObject data;
	
	public ApiException(int error, String errorx, Throwable e, JSONObject data){
		super(e);
		this.error = error;
		this.message = errorx;
		this.data = data;
	}


	
	public ApiException(int error, String errorx, JSONObject data){
		this(error, errorx, null, data);
	}


	public ApiException(int error, String errorx, Throwable e){
		this(error, errorx, e, null);
	}
	public ApiException(int error, String errorx){
		this(error, errorx, null, null);
	}
	
	public ApiException(ApiErrorEnum apiError) {
		this(apiError, null, null);
	}
	
	public ApiException(ApiErrorEnum apiError, Throwable e) {
		this(apiError, e, null);
	}
	
	public ApiException(ApiErrorEnum apiError, Throwable e, JSONObject data) {
		this(apiError == null ? ApiErrorEnum.COMMON_SERVICE_ERROR.getCode() : apiError.getCode(),
				apiError == null ? ApiErrorEnum.COMMON_SERVICE_ERROR.getMessage() : apiError.getMessage(), e, data);
	}
	
	public static ApiException buildApiException(ApiErrorEnum apiError, Object... strs) {
		String msg = String.format(apiError.getMessage(), strs);
		return new ApiException(apiError.getCode(), msg, null, null);
	}
	
	public static ApiException buildApiException(ApiErrorEnum apiError, Throwable e, Object... strs) {
		String msg = String.format(apiError.getMessage(), strs);
		return new ApiException(apiError.getCode(), msg, e, null);
	}
	
	public static ApiException buildApiException(ApiErrorEnum apiError, Throwable e, JSONObject data, Object... strs) {
		String msg = String.format(apiError.getMessage(), strs);
		return new ApiException(apiError.getCode(), msg, e, data);
	}
	
	public JSONObject buildDataFromApiException() {
		JSONObject eData = getData();
		if (eData == null) {
			eData = new JSONObject();
		}
		
		return eData;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}
}
