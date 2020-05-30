package com.deepthink.common.aop;

/**
 * @program: deepthink-common
 * @description: 日志监听者,实现者需实现此接口，并注入Spring容器中,其会被自动注册
 * @author: td
 * @create: 2020-03-20 11:47
 **/
public interface LogListener {
    /**
    * @Description: 此方法在此实例被注册后会被日志记录切面调用,注意此实现要求是线程安全的
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2020/3/20
    */
    void onLog(Log log);
}
