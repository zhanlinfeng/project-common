package com.deepthink.common.kafka;

import com.alibaba.fastjson.JSON;
import com.deepthink.common.web.ApiErrorEnum;
import com.deepthink.common.web.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * FileName : HMessageTemplate.java Description : 消息发送逻辑
 **/
@Component
public class KafkaMsgSender{

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaMsgSender.class);

	private long default_timeout = 1000L;


	public void send(KafkaTopicEnum topicEnum, Object msg) {
		String msgJsonStr = JSON.toJSONString(msg);
		ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(topicEnum.topic, msgJsonStr);
		try {
			future.get(default_timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | TimeoutException | ExecutionException e) {
			logger.error("发送失败,msg={}", msgJsonStr, e);
			throw new ApiException(ApiErrorEnum.COMMON_SERVICE_ERROR, e);
		}
	}

}
