package com.deepthink.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池组合实现，内部管理了一组线程池，线程池定义在{@link PoolEnum}。
 * 任务提交时，执行任务的线程池由{@link BaseTask#getPoolEnum()}
 * 来确定具体使用的线程池
 *
 * @program: admin_server
 * @description: 线程池组合
 * @author: td
 * @create: 2019-11-28 11:51
 **/
@Component
public class ExecutorComposite implements DisposableBean, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorComposite.class);

    private ConcurrentHashMap<PoolEnum, ExecutorService> executorMap;

    /**
     * 提交任务
     *
     * @Description:
     * @Param:
     * @return:
     * @Author: td
     * @Date: 2019/11/28
     */
    public void execute(RunnableTask task) {
        getExecutor(task.getPoolEnum()).execute(task);
    }

    /**
     * 提交任务
     *
     * @Description:
     * @Param:
     * @return:
     * @Author: td
     * @Date: 2019/11/28
     */
    public <V> Future<V> submit(CallableTask<V> task) {
        return getExecutor(task.getPoolEnum()).submit(task);
    }

    /**
     * 线程工厂
     */
    static class AdminThreadPoolFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        AdminThreadPoolFactory(PoolEnum taskEnum) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + taskEnum.poolName + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    private ExecutorService getExecutor(PoolEnum poolEnum) {
        if (poolEnum == null) {
            throw new NullPointerException("poolEnum为空！");
        }
        if (poolEnum.poolCoreSize <= 0) {
            throw new IllegalArgumentException("核心池大小不能为负数！");
        }
        ExecutorService executor = executorMap.get(poolEnum);
        if (executor == null) {
            synchronized (executorMap) {
                if ((executor = executorMap.get(poolEnum)) == null) {
                    executor = createExecutor(poolEnum);
                    executorMap.put(poolEnum, executor);
                }
            }
        }
        return executor;
    }

    private ExecutorService createExecutor(PoolEnum poolEnum) {
        return new ThreadPoolExecutor(poolEnum.poolCoreSize, poolEnum.poolCoreSize, 1000 * 100L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new AdminThreadPoolFactory(poolEnum));
    }

    @Override
    public void destroy() throws Exception {
        Collection<ExecutorService> executors = executorMap.values();
        for (ExecutorService executor : executors) {
            executor.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorMap = new ConcurrentHashMap<>(PoolEnum.values().length);
    }
}
