package com.deepthink.common.mqtt.http;

/**
 * @author td
 */
public class MqttHttpResult<D> {

    private int code;

    private D result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public D getResult() {
        return result;
    }

    public void setResult(D result) {
        this.result = result;
    }
}
