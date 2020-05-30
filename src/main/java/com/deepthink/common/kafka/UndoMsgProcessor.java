package com.deepthink.common.kafka;


import com.deepthink.common.thread.ExecutorComposite;
import com.deepthink.common.thread.PoolEnum;
import com.deepthink.common.thread.RunnableTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 未处理的消息处理器,内部维护了两个线程，一个用于添加未处理的任务，另一个用于分发任务
 *
 * @author td
 */
public class UndoMsgProcessor implements ApplicationListener<ApplicationReadyEvent> {


    @Autowired
    private MsgDispatcher msgDispatcher;

    @Autowired(required = false)
    private UndoMsgGetter undoMsgGetter;

    private final ConcurrentHashMap<Long, IKafkaUndoMsg> undoMsgs = new ConcurrentHashMap<>();

    @Autowired
    private ExecutorComposite executor;

    int maxNum = 100;

    private final BlockingQueue<IKafkaUndoMsg> undoMsgBeanBlockingQueue = new ArrayBlockingQueue<>(maxNum);

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private synchronized void addUndoMsg() {
        List<? extends IKafkaUndoMsg> undoMsgList = undoMsgGetter.getUndoMsg(50);
        for (IKafkaUndoMsg undoMsgBean : undoMsgList) {
            final long id = undoMsgBean.getId();
            IKafkaUndoMsg old = undoMsgs.putIfAbsent(id, undoMsgBean);
            // 如果put成功
            if (old == null) {
                boolean offered = undoMsgBeanBlockingQueue.offer(undoMsgBean);
                if (offered) {
                    logger.warn("队列已满，size={}", undoMsgBeanBlockingQueue.size());
                    undoMsgs.remove(id);
                }
            }
        }
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            logger.error("执行中断！", e);
        }
    }


    private void dispatch(IKafkaUndoMsg undoMsgBean) {
        msgDispatcher.dispatch(undoMsgBean.getTopic(), undoMsgBean.getMsg());
    }

    private void over(Long id) {
        undoMsgs.remove(id);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (undoMsgGetter == null) {
            logger.warn("没有配置未处理消息获取器！");
            return;
        }
        doAdd();
        doDispatch();
    }

    private void doDispatch() {
        executor.execute(new RunnableTask() {
            @Override
            public void invoke() {
                IKafkaUndoMsg undoMsg = undoMsgBeanBlockingQueue.poll();
                if (undoMsg != null) {
                    try {
                        dispatch(undoMsg);
                        undoMsgGetter.done(undoMsg);
                    } finally {
                        over(undoMsg.getId());
                    }
                } else {
                    logger.info("暂无可处理的任务！");
                }
            }

            @Override
            public PoolEnum getPoolEnum() {
                return PoolEnum.kafka_undo_msg;
            }
        });
    }

    private void doAdd() {
        executor.execute(new RunnableTask() {
            @Override
            public void invoke() {
                addUndoMsg();
            }

            @Override
            public PoolEnum getPoolEnum() {
                return PoolEnum.add_undo_msg;
            }
        });
    }

    public int getSize() {
        return undoMsgBeanBlockingQueue.size();
    }
}
