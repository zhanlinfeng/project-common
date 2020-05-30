package com.deepthink.common.mqtt;

/**
 * 主题转化器
* @Description:
* @Param:
* @return:
* @Author: td
* @Date: 2019/11/28
*/
public interface TopicConverter {
    String convert(MqttTopicEnum topicEnum);
}
