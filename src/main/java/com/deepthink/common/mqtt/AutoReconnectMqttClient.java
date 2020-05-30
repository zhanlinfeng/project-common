package com.deepthink.common.mqtt;


import com.deepthink.common.thread.ExecutorComposite;
import com.deepthink.common.thread.PoolEnum;
import com.deepthink.common.thread.RunnableTask;
import com.deepthink.common.util.StringUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @program: data_server
 * @description: 该实现能自动尝试重新连接。此实现只持有一个MqttClient(发布阻塞，订阅基于异步回调)，
 * 并且此类型是单例的（由容器的单例保证）。
 * @author: td
 * @create: 2019-10-15 16:07
 **/
public class AutoReconnectMqttClient implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(AutoReconnectMqttClient.class);

    protected  MqttClient mqttClient = null;

    private HashMap<String, List<MqttTopicHandler>> topicHandlerMap;

    @Autowired
    private ExecutorComposite executor;

    @Autowired(required = false)
    private List<MqttTopicHandler> topicHandlers;

    @Value("${mqtt.host.url:}")
    private String brokerAddress;

    @Value("${mqtt.username:}")
    private String userName;

    @Value("${mqtt.password:}")
    private String passWord;

    @Value("${mqtt.client.id:}")
    private String clientId;

    /**
     * 默认的服务质量(qos)
     */
    private int defaultQos = 2;


    public void publish(MqttTopicEnum topicEnum, Object data) {
        logger.info("发送消息开始，topic:{}", topicEnum);
        try {
            byte[] payload = topicEnum.getSerializer().serialize(data);
            publish(topicEnum.getTopic(), payload);
        } catch (MqttException e) {
            logger.error("发送消息：{}失败", data, e);
            throw new RuntimeException("发送消息失败");
        }
        logger.info("发送消息结束，msg:{}", data);
    }


    private void publish(String topic, Object data) {
        logger.info("发送消息开始，topic:{}", topic);
        try {
            byte[] payload = MqttTopicEnum.getDefaultSerializer().serialize(data);
            publish(topic, payload);
        } catch (MqttException e) {
            logger.error("发送消息：{}失败", e);
            throw new RuntimeException("发送消息失败");
        }
        logger.info("发送消息结束，msg:{}", data);
    }

    private void publish(String topic, byte[] payload) throws MqttException {
        mqttClient.publish(topic, payload, defaultQos, false);
    }
    /**
     * 该方法支持动态生成发布主题，通过实现{@link TopicConverter}
    * @Description:
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2019/11/28
    */
    public void publish(MqttTopicEnum topicEnum, MqttMsg msg, TopicConverter converter) throws MqttException {
        byte[] payload = topicEnum.getSerializer().serialize(msg);
        publish(converter.convert(topicEnum), payload);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    private void initTopicHandler() {
        topicHandlerMap = new HashMap<>(5);
        MqttTopicEnum[] topicEnums = MqttTopicEnum.values();
        if (topicEnums != null && topicEnums.length > 0) {
            if (!CollectionUtils.isEmpty(topicHandlers)) {
                for (MqttTopicHandler topicHandler : topicHandlers) {
                    for (MqttTopicEnum topicEnum : topicEnums) {
                        if (topicHandler.couldHandle(topicEnum) && topicEnum.getSubcribed()) {
                            String topic = topicEnum.getTopic();
                            List<MqttTopicHandler> handlers = topicHandlerMap.get(topic);
                            if (handlers == null) {
                                handlers = new ArrayList<>();
                                topicHandlerMap.put(topic, handlers);
                            }
                            handlers.add(topicHandler);
                        }
                    }
                }
            }
        }
    }

    private void init() {
        if (!configRight()) {
            return;
        }
        initTopicHandler();
        createMqttClient();
    }

    private boolean configRight() {
        return StringUtils.isNotEmpty(brokerAddress)
                && StringUtils.isNotEmpty(userName)
                && StringUtils.isNotEmpty(passWord)
                && StringUtils.isNotEmpty(clientId);
    }

    private void createMqttClient() {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            logger.info("MqttDeviceComponent init.brokerAddress:" + brokerAddress + ",serverNodeId:" + clientId);
            mqttClient = new MqttClient(brokerAddress, clientId, persistence);
            MqttConnectOptions connOpts = getMqttConnectOptions();
            // 注意，必须先于订阅前设置回调
            mqttClient.setCallback(new MqttMsgCallback());
            mqttClient.connect(connOpts);
            // 监听所有配置好的主题
            subcribe();
            logger.info("MQTT init success.");
        } catch (MqttException e) {
            logger.error("MQTT init failed:{}" ,e.getMessage(), e);
        }
    }

    private void subcribe() throws MqttException {
        for (MqttTopicEnum mqttTopicEnum : MqttTopicEnum.values()) {
            if (mqttTopicEnum.getSubcribed()) {
                mqttClient.subscribe(mqttTopicEnum.getTopic());
            }
        }
    }






    private MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // 保持回话状态
        connOpts.setCleanSession(false);
        // 设置超时时间
        connOpts.setConnectionTimeout(10);
        // 设置会话心跳时间
        connOpts.setKeepAliveInterval(20);
        connOpts.setUserName(userName);
        connOpts.setPassword(passWord.toCharArray());
        return connOpts;
    }

    /**
    * @Description: 容器销毁时，关闭连接
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2019/10/15
    */
    @Override
    public void destroy() throws Exception {
        mqttClient.disconnect();
        mqttClient.close(true);
    }
    /**
    * @Description: 必要时重新连接mqtt服务器
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2019/10/15
    */
    public void reconnectIfNecessary() {
        if (!mqttClient.isConnected()) {
            try {
                logger.warn("mqtt服务器连接断开，开始重连");
                mqttClient.connect(getMqttConnectOptions());
                subcribe();
                logger.info("mqtt服务器连接成功");
            } catch (MqttException e) {
                logger.error("mqtt服务器重连失败！", e);
            }
        }
    }


    class MqttMsgCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable cause) {
            logger.error("mqtt连接断开", cause);
            reconnectIfNecessary();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            logger.info("消息到达,topic={}", topic);
            List<MqttTopicHandler> handlers = topicHandlerMap.get(topic);
            MqttTopicEnum topicEnum = MqttTopicEnum.getEnumByTopic(topic);
            if (CollectionUtils.isEmpty(handlers)) {
                logger.warn("没有找到消息的处理器，topic={}", topic);
                return;
            }
            // 分发给所有可以处理的该消息的处理器
            for (MqttTopicHandler handler : handlers) {
                // 用异步的方式，不阻塞mqtt客户端接收线程
                executor.execute(new RunnableTask() {
                    @Override
                    public void invoke() {
                        handler.handle(message, topicEnum);
                    }

                    @Override
                    public PoolEnum getPoolEnum() {
                        return PoolEnum.generic_pool;
                    }
                });
            }
            logger.info("消息分发完成,topic={}", topic);
        }


        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            try {
                logger.info("消息topics={}, msg={}推送完成", token.getTopics(), token.getMessage());
            } catch (MqttException e) {
                logger.error("消息推送失败！", e);
            }
        }
    }


}
