package com.deepthink.common.aop;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: deepthink-common
 * @description: 日志实体
 * @author: td
 * @create: 2020-03-20 11:49
 **/
public class Log {

    private String errorMsg;

    private String userName;

    private Date startTime;

    private Date endTime;

    private long duration;

    private boolean invokeSuccess;

    private String className;

    private String ip;

    private String methodName;



    public Object getResult() {
        return result;
    }

    public String getObjectStr() {
        if (result == null) {
            return null;
        }
       return JSON.toJSONString(result);
    }

    public void setResult(Object result) {
        this.result = result;
    }

    private Object result;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isInvokeSuccess() {
        return invokeSuccess;
    }

    public void setInvokeSuccess(boolean invokeSuccess) {
        this.invokeSuccess = invokeSuccess;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    private Map<String, Object> argNameAndValue;

    public void setArg(String name, Object value) {
        if (argNameAndValue == null) {
            argNameAndValue = new HashMap<>(5);
        }
        argNameAndValue.put(name, value);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Map<String, Object> getArgNameAndValue() {
        return argNameAndValue;
    }

    public String argsToString() {

        return JSON.toJSONString(argNameAndValue);
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
