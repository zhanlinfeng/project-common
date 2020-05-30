package com.deepthink.common.aop;

import com.deepthink.common.thread.ExecutorComposite;
import com.deepthink.common.thread.PoolEnum;
import com.deepthink.common.thread.RunnableTask;
import com.deepthink.common.util.TokenUtil;
import com.deepthink.common.web.RequestThreadLocal;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @program: vending_admin
 * @description: 方法日志记录切面
 * @author: td
 * @create: 2020-03-20 11:34
 **/
@Aspect
@Component
public class LogAdvice {

    private static final Logger logger = LoggerFactory.getLogger(LogAdvice.class);

    SqlSessionFactory sqlSessionFactory;
    @Autowired(required = false)
    private List<LogListener> logListeners;

    @Autowired
    private ExecutorComposite executor;


    @Around("@annotation(com.deepthink.common.aop.NeedLog)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Date startTime = new Date();
        // 获取请求参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringTypeName();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Log requestLog = new Log();
        requestLog.setUserName(TokenUtil.getUserName());
        for (int i = 0; i < args.length; i++) {
            String parameterName = parameterNames[i];
            Object parameterValue = args[i];
            requestLog.setArg(parameterName, parameterValue);
        }
        // 设置其他必要属性
        requestLog.setMethodName(methodName);
        requestLog.setClassName(className);
        requestLog.setStartTime(startTime);
        requestLog.setUserName(RequestThreadLocal.getUserName());
        requestLog.setIp(RequestThreadLocal.getIp());
        Object result = null;
        try {
            // 执行方法
            result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            requestLog.setInvokeSuccess(false);
            requestLog.setErrorMsg(throwable.getMessage());
            // 不可以把这个异常吞掉
            throw throwable;
        } finally {
            Date endTime = new Date();
            requestLog.setInvokeSuccess(true);
            requestLog.setEndTime(endTime);
            requestLog.setDuration(endTime.getTime() - startTime.getTime());
            requestLog.setResult(result);
            broadcast(requestLog);
        }
    }

    /**
     * @Description: 把日志广播给关注者，这里采用异步的方式，不影响主线程的执行
     * @Param:
     * @return:
     * @Author: td
     * @Date: 2020/3/20
     */
    private void broadcast(Log log) {
        for (LogListener logListener :
                logListeners) {
            executor.execute(new RunnableTask() {
                @Override
                public void invoke() {
                    try {
                        logListener.onLog(log);
                    } catch (Throwable throwable) {
                        logger.error("日志广播失败， logClass={}", logListener.getClass(), throwable);
                    }
                }

                @Override
                public PoolEnum getPoolEnum() {
                    return PoolEnum.log_pool;
                }
            });
        }
    }
}


