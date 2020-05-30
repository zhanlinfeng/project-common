package com.deepthink.common.mqtt.conf;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 使用此注解引入mqtt组件
 * @author td
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MqttConfiguration.class)
public @interface EnableMqtt {
}
