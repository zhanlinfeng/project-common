package com.deepthink.common.mqtt;

public interface MqttMsgDeserializer {
    /**
     *
     * @param data serialized bytes
     * @return deserialized typed data
     */
    <T> T deserialize(byte[] data, Class<T> clazz);


}
