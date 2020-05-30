package com.deepthink.common.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 消息分发器
 * @author Administrator
 */
public class MsgDispatcher {

    @Autowired(required = false)
    private List<KafkaMsgListener> messageListeners;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public void dispatch(ConsumerRecord<Integer, String> record) {
        dispatch(record.topic(), record.value());
    }


    public void dispatch(String topic, String msg) {
        if (CollectionUtils.isEmpty(messageListeners)) {
            logger.warn("没有配置消息监听器！");
            return;
        }
        for (KafkaMsgListener messageLister : messageListeners) {
            // 分发给可以处理的handler
            if (messageLister.couldHandle(KafkaTopicEnum.getEnumByTopic(topic))) {
                messageLister.onMessage(topic, msg);
            }
        }
    }

    public List<KafkaMsgListener> getMessageListeners() {
        return messageListeners;
    }
}
