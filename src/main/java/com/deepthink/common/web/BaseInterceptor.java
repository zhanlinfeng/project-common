package com.deepthink.common.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static com.deepthink.common.web.RequestThreadLocal.*;

/**
 * @author : td
 * @Copyright : Deepthink.ai All Rights Reserved
 * @Company : deepthink.ai
 **/
public class BaseInterceptor extends HandlerInterceptorAdapter {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            //response.setHeader("Access-Control-Allow-Origin", "*");// 支持跨域请求，线上环境需要删除\
            requestStart(request);
            StringBuilder sb = new StringBuilder();
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                sb.append(key + ":" + value + ",");
            }

            logger.info("header params:" + sb.toString());
            return super.preHandle(request, response, handler);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
        endTime(request);
    }

    /**
     * 请求开始计时
     *
     * @param request
     */
    protected void requestStart(HttpServletRequest request) {
        start(request);
    }

    /**
     * 请求结束计时
     *
     * @param request
     */
    protected void endTime(HttpServletRequest request) {
        logger.info(request.getRequestURI() + " [qid:" + getRequestId() + "]" + " ["
                + end() + "ms]");
    }

}
