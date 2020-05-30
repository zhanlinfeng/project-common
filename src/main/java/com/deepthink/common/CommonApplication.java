package com.deepthink.common;

import com.deepthink.common.kafka.config.EnableKafkaProducer;
import com.deepthink.common.kafka.config.EnabledKafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableKafkaProducer
@EnabledKafkaConsumer
public class CommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class);
    }
}
