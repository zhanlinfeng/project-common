package com.deepthink.common.mqtt;

/**
 * @program: fridgeserver
 * @description: 消息序列化器接口
 * @author: zxb
 * @create: 2019-10-24 13:31
 **/
public interface MqttMsgSerializer {
    /**
     *
     * @return serialized bytes
     */
    public byte[] serialize(Object data);
}
