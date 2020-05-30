package com.deepthink.common.kafka;

import org.springframework.stereotype.Component;

@Component
class DemoListener extends AbstractKafkaMessageListener<String>{
    @Override
    protected void doHandle(KafkaMsg<String> kafkaMsg) {

    }

    @Override
    public boolean couldHandle(KafkaTopicEnum topic) {
        return false;
    }
}
