package com.deepthink.common.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @program: fridgeserver
 * @description: mqtt主题处理抽象基类
 * @author: td
 * @create: 2019-10-24 17:40
 **/
public abstract class AbstractMqttTopicHandler<D> implements MqttTopicHandler {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MqttContext mqttContext;

    /**
     * msgid map to CountDownLatch
     */
    private final ConcurrentHashMap<Long, CountDownLatch> callbackLatchs = new ConcurrentHashMap<>();

    /**
     * @Description: 阻塞直到目标消息达到
     * @Param:
     * @return:
     * @Author: td
     * @Date: 2019/10/30
     */
    @Override
    public void await(Long msgId, Long timeOut, TimeUnit timeUnit) {
        CountDownLatch latch = new CountDownLatch(1);
        callbackLatchs.put(msgId, latch);
        try {
            latch.await(timeOut, timeUnit);
        } catch (InterruptedException e) {
            logger.error("等待中断", e);
        } finally {
            // 必须清除
            callbackLatchs.remove(msgId);
        }
    }

    @Override
    public void release(Long msgId) {
        CountDownLatch latch = callbackLatchs.get(msgId);
        if (latch != null) {
            latch.countDown();
        }
    }

    @Override
    public final void handle(MqttMessage message, MqttTopicEnum mqttTopicEnum) {
        MqttMsg<D> msg = new MqttMsg<>();
        Class<?> clz = getClass();
        Type genericSuperclass = clz.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        // 获得父接口的泛型信息
        Type genericType = parameterizedType.getActualTypeArguments()[0];
        if (genericType instanceof Class) {
            @SuppressWarnings("")
            Class<D> dataClz = (Class) genericType;
            D data = mqttTopicEnum.getMsgDeserializer().deserialize(message.getPayload(), dataClz);
            msg.setOriginalMessage(message);
            msg.setTopic(mqttTopicEnum);
            msg.setData(data);
        } else {
            logger.error("检查Mqtt的消息泛型，请使用简单的java类型！");
            return;
        }
        doHandle(msg);
    }

    /**
     * @Description: 真实处理逻辑模板
     * @Param:
     * @return:
     * @Author: td
     * @Date: 2019/11/26
     */
    protected abstract void doHandle(MqttMsg<D> msg);


}
