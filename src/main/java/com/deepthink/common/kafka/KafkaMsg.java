package com.deepthink.common.kafka;

/**
 * kafka消息封装
 **/
public class KafkaMsg<D>{

	private static final long serialVersionUID = -5634954324205496909L;

	private D data;

	private  KafkaTopicEnum topic;

	public KafkaTopicEnum getTopic() {
		return topic;
	}

	KafkaMsg(KafkaTopicEnum topic) {
		this.topic = topic;
	}

	public D getData() {
		return data;
	}

	void setData(D data) {
		this.data = data;
	}
}
