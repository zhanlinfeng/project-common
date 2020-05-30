package com.deepthink.common.web;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * API响应结果
 *
 * @author : td
 * @version : 1.0
 * @Copyright :
 */
public class ApiResult<D> extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;

    // 响应状态码：0成功，非0为失败
    private static final String CODE = "code";
    // 响应信息
    private static final String MSG = "msg";
    // 响应数据
    private static final String DATA = "data";
    // 系统时间，格式：yyyyMMddHHmmss
    private static final String TIMESTAMP = "timestamp";


    /**
     * 得到默认的成功响应
     *
     * @author Doug Lea
     */
    public ApiResult() {
        this(ApiResultEnum.result_200);
    }

    public ApiResult(D data) {
        this(ApiResultEnum.result_200, data);
    }

    /**
     * @param resultEnum dfs
     */
    public ApiResult(ApiResultEnum resultEnum) {
        this(resultEnum, null);
    }

    public ApiResult(ApiResultEnum resultEnum, D obj) {
        this.put(CODE, resultEnum.getCode());
        this.put(MSG, resultEnum.getMsg());
        this.put(TIMESTAMP, DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
        this.put(DATA, obj);
    }

    public ApiResult(Integer code, String msg) {
        this.put(CODE, code);
        this.put(MSG, msg);
        this.put(TIMESTAMP, DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
    }

    public static ApiResult commonResutl500() {
        return new ApiResult(ApiResultEnum.result_500);
    }

    public static ApiResult commonResutl404() {
        return new ApiResult(ApiResultEnum.result_404);
    }

    public static ApiResult commonResutl200() {
        return new ApiResult();
    }

    public int getCode() {
        return (Integer) get(CODE);
    }

    public String getMsg() {
        return (String) get(MSG);
    }

    public String getTimestamp() {
        return (String) get(TIMESTAMP);
    }

    public Object getData() {
        return get(DATA);
    }

    public void setData(Object obj) {
        this.put(DATA, obj);
    }

    public ApiResult append(String key, Object obj) {
        this.put(key, obj);
        return this;
    }

    public String toString(String callback) {
        String jsonString = JSON.toJSONString(this);
        return callback + "(" + jsonString + ")";
    }


}
