package com.deepthink.common.kafka;

import com.deepthink.common.thread.ExecutorComposite;
import com.deepthink.common.thread.PoolEnum;
import com.deepthink.common.thread.RunnableTask;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @Description: kafka消息处理流程启动器
 * @Author: td
 * @Date: 2020/4/3
 */
public class KafkaMsgProcessor implements ApplicationListener<ApplicationReadyEvent> {


    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.kafka.bootstrap-servers:120.79.212.203:30730}")
    private List<String> bootstrapServers;
    @Value("${spring.kafka.topics:test}")
    private String topic;
    @Value("${kafka.group.id:1}")
    private String groupId;

    @Autowired
    private ExecutorComposite executor;


    @Autowired
    private MsgDispatcher msgDispatcher;

    @Autowired
    private UndoMsgGetter undoMsgGetter;

    private class MsgReceiver extends RunnableTask {

        final KafkaConsumer<Integer, String> consumer;

        MsgReceiver(KafkaConsumer<Integer, String> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void invoke() {
            while (!Thread.interrupted()) {
                try {
                    final ConsumerRecords<Integer, String> records = consumer.poll(100);
                    for (ConsumerRecord<Integer, String> record : records) {
                        handleRecord(record);
                    }
                } catch (Throwable t) {
                    logger.error("数据处理出错!", t);
                }
            }
            onOver();
        }

        private void handleRecord(ConsumerRecord<Integer, String> record) {
            executor.execute(new RunnableTask() {
                @Override
                public void invoke() {
                    // 分发消息
                    try {
                        msgDispatcher.dispatch(record);
                    } catch (Exception ex) {
                        logger.error("错误消息数据出错", ex);
                        if (undoMsgGetter != null) {
                            logger.error("消息处理异常，添加未完成记录");
                            undoMsgGetter.addUndoMsg(record.topic() , record.value(), ex.getMessage());
                        }
                    }
                }

                @Override
                public PoolEnum getPoolEnum() {
                    return PoolEnum.kafka_pool;
                }
            });
        }


        private void onOver() {
            // 提交偏移量
            consumer.commitSync();
            // 关闭消费者
            consumer.close();
            // 恢复中断信号
            Thread.currentThread().interrupt();
        }

        @Override
        public PoolEnum getPoolEnum() {
            return PoolEnum.kafka_selector_pool;
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("开始启动,正在检查");
        if (!CollectionUtils.isEmpty(msgDispatcher.getMessageListeners())) {
            logger.info("检测到" + msgDispatcher.getMessageListeners().size() + "个监听器,开始监听");
            logger.info("启动的topic名称：" + topic);
            if (!StringUtils.isEmpty(topic)) {
                final String[] topics = topic.split("\\,");
                Properties props = new Properties();
                props.put("bootstrap.servers", bootstrapServers);
                props.put("group.id", StringUtils.isEmpty(groupId) ? "0" : groupId);
                props.put("enable.auto.commit", "true");
                props.put("auto.commit.interval.ms", "1000");
                props.put("session.timeout.ms", "30000");
                props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
                props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                logger.info("启动的groupId名称：" + groupId + " props=" + props);
                KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(props);
                consumer.subscribe(Arrays.asList(topics));
                executor.execute(new MsgReceiver(consumer));
            } else {
                logger.error("没有topic");
            }
        } else {
            logger.warn("没有配置消息监听者");
        }
    }


}
