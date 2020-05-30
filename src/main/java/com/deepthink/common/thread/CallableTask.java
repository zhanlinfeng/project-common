package com.deepthink.common.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;

/**
 * @program: admin_server
 * @description:
 * @author: td
 * @create: 2019-11-28 14:18
 **/
public abstract class CallableTask<V> extends BaseTask implements Callable<V> {

    @Override
    public V call() throws Exception {
        V result = null;
        try{
            long now = System.currentTimeMillis();
            logger.info("入队到处理时间为：{}ms", now - getCreateTime());
            result = invoke();
            logger.info("处理耗时：{}ms", System.currentTimeMillis() - now);
        }catch(Exception e){
            logger.error("执行任务错误", e);
            throw e;
        }
        return result;
    }

    /**
     * 此方法为业务处理的模板方法，实现业务处理逻辑
     * @Description:
     * @Param:
     * @return:
     * @Author: td
     * @Date: 2019/11/28
     */
    public abstract V invoke() throws BrokenBarrierException, InterruptedException;
}
