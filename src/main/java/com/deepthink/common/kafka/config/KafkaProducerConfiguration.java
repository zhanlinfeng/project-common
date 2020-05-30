package com.deepthink.common.kafka.config;

import com.deepthink.common.kafka.KafkaMsgSender;
import org.springframework.context.annotation.Bean;

public class KafkaProducerConfiguration {
    @Bean
    public KafkaMsgSender getKafkaMsgSender() {
        return new KafkaMsgSender();
    }
}
