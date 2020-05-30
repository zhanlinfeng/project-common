package com.deepthink.common.kafka;

import com.deepthink.common.mqtt.MqttTopicEnum;

/**
 * FileName : KafkaEnum.java Description :
 * 
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 * @author : td
 **/
public enum  KafkaTopicEnum {
	/**测试*/
	test("test"),

	order_create("order_create"),

	order_update("order_update"),
	;
	KafkaTopicEnum(String topic) {
		this.topic = topic;
	}
	String topic;


	public static KafkaTopicEnum getEnumByTopic(String topic) {
		for (KafkaTopicEnum topicEnum : KafkaTopicEnum.values()) {
			if (topicEnum.topic.equals(topic)) {
				return topicEnum;
			}
		}
		return null;
	}
}
