package com.deepthink.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: admin_server
 * @description: 任务基类
 * @author: td
 * @create: 2019-11-28 11:31
 **/
abstract class BaseTask {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected long createTime = System.currentTimeMillis();


    public abstract PoolEnum getPoolEnum();

    public long getCreateTime(){
        return createTime;
    }

}
