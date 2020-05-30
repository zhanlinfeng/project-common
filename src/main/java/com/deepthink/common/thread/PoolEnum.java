package com.deepthink.common.thread;

/**
 * @program: admin_server
 * @description: 执行器枚举
 * @author: td
 * @create: 2019-11-28 11:25
 **/
public enum PoolEnum {
    /**执行器监控任务*/
    monitor_task(1, "monitor"),
    /**
     * 通用的线程池
     */
    generic_pool(15, "generic"),

    mqtt_pool(10, "mqtt"),

    kafka_pool(10, "kafka"),
    /**
     * 仅用作单元测试，不要使用它
     */
    test_pool(10, "test"),

    kafka_selector_pool(1, "kafka_selector"),

    log_pool(1, "log"),

    kafka_undo_msg(3, "kafka_undo_msg"),

    add_undo_msg(1, "add_undo_msg"),
    ;
    PoolEnum(int poolCoreSize, String poolName) {
        this.poolCoreSize = poolCoreSize;
        this.poolName = poolName;
    }
    int poolCoreSize;

    String poolName;
}
