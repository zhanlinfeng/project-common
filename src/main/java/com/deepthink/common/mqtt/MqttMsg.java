package com.deepthink.common.mqtt;

import com.alibaba.fastjson.annotation.JSONField;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * mttq消息的封装,默认的消息反序列化器 {@link MqttTopicEnum.DefaultDeserializer } 要求实现类是POJO
 * @program: fridgeserver
 * @description:
 * @author: td
 * @create: 2019-10-24 12:58
 **/
public class MqttMsg<D> {

    @JSONField(serialize = false, deserialize = false)
    private MqttMessage originalMessage;

    @JSONField(serialize = false, deserialize = false)
    private MqttTopicEnum topic;

    private Long contextId;

    public Long getContextId() {
        return contextId;
    }

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }

    /**
    * @Description: 获得原始的mqtt消息
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2019/11/26
    */
    public final MqttMessage getOriginalMessage() {
        return originalMessage;
    }

    final void setOriginalMessage(MqttMessage originalMessage) {
        this.originalMessage = originalMessage;
    }

    public final MqttTopicEnum getTopic() {
        return topic;
    }

    final void setTopic(MqttTopicEnum topic) {
        this.topic = topic;
    }

    private D data;

    public D getData() {
        return data;
    }

    void setData(D data) {
        this.data = data;
    }
}
