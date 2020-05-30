package com.deepthink.common.mqtt.conf;

import com.deepthink.common.mqtt.AutoReconnectMqttClient;
import com.deepthink.common.mqtt.http.MqttHttpClientV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author td
 */
public class MqttConfiguration {
    @Bean("MqttHttpClientV2")
    public MqttHttpClientV2 getMqttHttpClientV2() {
        return new MqttHttpClientV2();
    }

    @Bean
    public AutoReconnectMqttClient getAutoReconnectMqttClient() {
        return new AutoReconnectMqttClient();
    }
}
