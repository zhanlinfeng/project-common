package com.deepthink.common.kafka;


interface KafkaMsgListener {
	

     void onMessage(String topic, String message);


	/**
	 * 是否可以处理这个消息中心
	 */
	 boolean couldHandle(KafkaTopicEnum topic);
}
