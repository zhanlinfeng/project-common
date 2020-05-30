package test;


import com.deepthink.common.thread.CallableTask;
import com.deepthink.common.thread.ExecutorComposite;
import com.deepthink.common.thread.PoolEnum;
import com.deepthink.common.thread.RunnableTask;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: admin_server
 * @description: 线程池组合测试
 * @author: td
 * @create: 2019-11-28 15:05
 **/
public class ExecutorCompositeTest extends AutoRollbackBaseDatabaseTest {

    @Autowired
    private ExecutorComposite executor;

    private volatile boolean executed = false;
    @Test
    public void testExcutorDestroy() throws Exception {
        executor.destroy();
    }

    /**
     * 测试线程此执行基本功能
    * @Description:
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2019/11/28
    */
    @Test
    public void testExcuteSimple() throws Exception {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        executor.execute(new RunnableTask() {
            @Override
            public void invoke() {
                try {
                    executed = true;
                    cyclicBarrier.await();
                    logger.info("over");
                    System.out.println("over");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public PoolEnum getPoolEnum() {
                return PoolEnum.generic_pool;
            }
        });
        cyclicBarrier.await(100, TimeUnit.MILLISECONDS);
        Assert.assertTrue(executed);
        executor.destroy();
    }
    /**
     * 测试线程池提交基本功能
    * @Description:
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2019/11/28
    */
    @Test
    public void testSubmitSimple() throws Exception {
        Future<Boolean> future =  executor.submit(new CallableTask<Boolean>() {
            @Override
            public PoolEnum getPoolEnum() {
                return PoolEnum.generic_pool;
            }

            @Override
            public Boolean invoke() {
                return true;
            }
        });
        Assert.assertTrue(future.get());
        Assert.assertTrue(executed);
        executor.destroy();
    }

    @Test
    public void testMulExecutor() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(6);
        for (int i = 0; i < 3; i++) {
            Future<Boolean> future =  executor.submit(new CallableTask<Boolean>() {
                @Override
                public PoolEnum getPoolEnum() {
                    return PoolEnum.generic_pool;
                }

                @Override
                public Boolean invoke() throws BrokenBarrierException, InterruptedException {
                    System.out.println(counter.incrementAndGet());
                    latch.countDown();
                    return true;
                }
            });
            Future<Boolean> future2 =  executor.submit(new CallableTask<Boolean>() {
                @Override
                public PoolEnum getPoolEnum() {
                    return PoolEnum.test_pool;
                }

                @Override
                public Boolean invoke() throws BrokenBarrierException, InterruptedException {
                    System.out.println(counter.incrementAndGet());
                    latch.countDown();
                    return true;
                }
            });

        }
        Thread.sleep(1000);
        latch.await();
        Assert.assertTrue(counter.get() == 6);
        executor.destroy();
    }
}
