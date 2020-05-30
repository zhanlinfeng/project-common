package com.deepthink.common.kafka;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * FileName : AbstractHMessageListener.java Description : 抽象的业务对象
 *所有处理kafka消息的逻辑都应该继承此类，并且使用泛型D指定消息的类型，注意上一层接口不容许在业务代码内
 * 直接继承
 * @author : td
 **/
public abstract class AbstractKafkaMessageListener<D> implements KafkaMsgListener {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 	缓存data class，防止重复反射
 	 */
    private volatile Class<D> dataType;

    @Autowired(required = false)
    private UndoMsgGetter undoMsgGetter;

    private Class<D> getDataType() {
        if (dataType != null) {
            return dataType;
        }
        synchronized (this) {
            // double check
            if (dataType != null) {
                return dataType;
            }
            Class<?> clz = getClass();
            Type genericSuperclass = clz.getGenericSuperclass();
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            // 获得父接口的泛型信息
            Type genericType = parameterizedType.getActualTypeArguments()[0];
            if (genericType instanceof Class) {
                dataType = (Class<D>) genericType;
            } else {
                logger.error("检查kafka的消息泛型，请使用简单的java类型！");
            }
        }
        return dataType;
    }

    @Override
    public void onMessage(String topic, String message) {
        D data = JSON.parseObject(message, getDataType());
        KafkaMsg<D> kafkaMsg = new KafkaMsg<>(KafkaTopicEnum.getEnumByTopic(topic));
        kafkaMsg.setData(data);
        doHandle(kafkaMsg);
    }

    protected abstract void doHandle(KafkaMsg<D> kafkaMsg);
}
