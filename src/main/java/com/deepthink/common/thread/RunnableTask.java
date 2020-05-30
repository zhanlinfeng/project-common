package com.deepthink.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: admin_server
 * @description:
 * @author: td
 * @create: 2019-11-28 11:30
 **/
public abstract class RunnableTask extends BaseTask implements Runnable{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public final void run(){
        try{
            long now = System.currentTimeMillis();
            logger.info("入队到处理时间为：{}ms", now - getCreateTime());
            invoke();
            logger.info("处理耗时：{}ms", System.currentTimeMillis() - now);
        }catch(Exception e){
            // 这里我们吞掉这个异常，否则将导致线程死亡
            logger.error("执行任务错误", e);
        }
    }

    /**
     * 此方法为业务处理的模板方法，实现业务处理逻辑
     * @Description:
     * @Param:
     * @return:
     * @Author: td
     * @Date: 2019/11/28
     */
    public abstract void invoke();
}
