package com.deepthink.common.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

/**
 * @program: data_server
 * @description: 主题消息处理器根接口,实现请继承其抽象实现 {@link AbstractMqttTopicHandler}
 * @author: td
 * @create: 2019-10-15 16:07
 **/
interface  MqttTopicHandler {
    /**
    * @Description: 消息处理逻辑
    * @Param:  msg 消息
    * @return:
    * @Author: td
    * @Date: 2019/10/15
     * @param message 原始的mqtt消息
     */
    void handle(MqttMessage message, MqttTopicEnum mqttTopicEnum);



    /**
    * @Description: 返回该处理器是否可以处理该消息,多个处理器同时处理一个消息会导致重复消费
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2019/10/15
    */
    boolean couldHandle(MqttTopicEnum topicEnum);
    /**
    * @Description: 阻塞调用者，直到目标消息到达或超时
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2019/10/30
    */
    void await(Long msgId, Long timeOut, TimeUnit timeUnit);
    /**
    * @Description: 释放目标消息的等待者，如果该消息没有等待则不做任何操作
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2019/10/30
    */
    void release(Long msgId);


}
