package com.deepthink.common.kafka.config;


import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用kafka消息消费组件
 * @author td
 */
@Import(KafkaConsumerConfiguration.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnabledKafkaConsumer {
}
