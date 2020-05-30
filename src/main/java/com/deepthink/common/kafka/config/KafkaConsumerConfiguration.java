package com.deepthink.common.kafka.config;

import com.deepthink.common.kafka.KafkaMsgProcessor;
import com.deepthink.common.kafka.MsgDispatcher;
import com.deepthink.common.kafka.UndoMsgProcessor;
import org.springframework.context.annotation.Bean;

public class KafkaConsumerConfiguration {

    @Bean
    public KafkaMsgProcessor getKafkaMsgProcessingStarter() {
        return new KafkaMsgProcessor();
    }

    @Bean
    public MsgDispatcher getMsgDispatcher() {
        return new MsgDispatcher();
    }

    @Bean
    public UndoMsgProcessor getUndoMsgProcessor() {
        return new UndoMsgProcessor();
    }
}
