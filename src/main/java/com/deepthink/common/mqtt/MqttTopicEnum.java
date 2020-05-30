package com.deepthink.common.mqtt;

import com.alibaba.fastjson.JSON;

/**
* @Description: topic枚举，
* @Author: td
* @Date: 2019/10/15
*/
public enum MqttTopicEnum {
    /**测试*/
    test_topic("test", "test", true, false),

    test_topic2("test", "test", false, false),

    test_shared_topic("test_shared_topic", "test", true, true),
    ;




    MqttTopicEnum(String topic, String desc, boolean subcribed, boolean shared) {
        if (shared) {
            this.topic = "$queued/" + topic;
        } else {
            this.topic = topic;
        }
        this.desc = desc;
        this.subcribed = subcribed;
    }

    MqttTopicEnum(String topic, String desc,
                  Class<? extends MqttMsgDeserializer> msgDeserializer,
                  Class<? extends MqttMsgSerializer> msgSerializer) {
        this.topic = topic;
        this.desc = desc;
        this.msgDeserializer = msgDeserializer;
        this.msgSerializer = msgSerializer;
    }

    private String topic;
    /** 作用描述*/
    private String desc;


    /**
     * 是否监听该主题
     */
    private Boolean subcribed;

    /**
     * 是否共享订阅
     * */
    private Boolean shared;

    private Class<? extends MqttMsgDeserializer> msgDeserializer;

    private Class<? extends MqttMsgSerializer> msgSerializer;

    public String getTopic() {
        return topic;
    }

    public String getDesc() {
        return desc;
    }

    public static MqttMsgDeserializer getDefaultDeserializer() {
        return new DefaultDeserializer();
    }

    public static MqttMsgSerializer getDefaultSerializer() {
        return new DefaultSerializer();
    }

    public MqttMsgDeserializer getMsgDeserializer() {
        if (msgDeserializer == null) {
            return getDefaultDeserializer();
        }
        try {
            return msgDeserializer.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("获取反序列化器失败");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取反序列化器失败");
        }
    }

    public MqttMsgSerializer getSerializer() {
        if (msgSerializer == null) {
            return new DefaultSerializer();
        }
        try {
            return msgSerializer.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("获取序列化器失败");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取序列化器失败");
        }
    }


    public static MqttTopicEnum getEnumByTopic(String topic) {
        for (MqttTopicEnum topicEnum : MqttTopicEnum.values()) {
            if (topicEnum.getTopic().equals(topic)) {
                return topicEnum;
            }
        }
        return null;
    }

    /**
     * @program: fridgeserver
     * @description: 默认的消息反序列化器，json格式反序列化
     * @author: zxb
     * @create: 2019-10-24 13:35
     **/
    static class DefaultDeserializer implements MqttMsgDeserializer {
        @Override
        public <T> T deserialize(byte[] data, Class<T> clazz) {
            if (data == null) {
                throw new NullPointerException("输入数据为空");
            }
            if (clazz == null) {
                throw new NullPointerException("目标类型为空");
            }
            String msgStr = new String(data);
            return JSON.parseObject(msgStr, clazz);
        }
    }

    /**
     * @program: fridgeserver
     * @description: 默认的mqtt消息序列化器
     * @author: zxb
     * @create: 2019-11-02 17:49
     **/
    static class DefaultSerializer implements MqttMsgSerializer {
        @Override
        public byte[] serialize(Object data) {
            String str = JSON.toJSONString(data);
            return str.getBytes();
        }
    }

    public Boolean getSubcribed() {
        return subcribed;
    }
}
