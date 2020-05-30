package com.deepthink.common.web;


import com.deepthink.common.util.TokenUtil;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请求本地存储，此类的实现是为了在线程本地存储中请求通用的参数(头部参数（优先）或请求参数)，这样在这个线程执行中够可以
 * 通过此类获取必要参数，而不是通过参数的方式一级一级的往下透传。请注意在异步线程中不会有任何返回值，因为
 * 此类是基于{@link ThreadLocal}实现的。请求进入是由拦截器为此类添加参数，请求结束时也会显式的清理缓存(线程不会一定会被销毁
 * 不显式清理可能会导致过期数据)。
 *
 * @author: td
 * @create: 2019-11-22 11:49
 **/
public class RequestThreadLocal {

    private static ConcurrentHashMap<String, ThreadLocal<String>> paramsCache = new ConcurrentHashMap<>();

    /**
     * 自动增长的请求ID
     */
    private static final AtomicInteger seq = new AtomicInteger(0);

    /**
     * 请求开始的时间
     */
    private static final Map<Integer, Long> timeCaches = new ConcurrentHashMap<Integer, Long>();

    private static final ThreadLocal<Integer> requestIds = new ThreadLocal<>();

    private static ThreadLocal<String> ips = new ThreadLocal<>();

    public final static CachedHeadEunm locale = CachedHeadEunm.locale;

    /**
     * 配置需要被缓存的头部
     */
    enum CachedHeadEunm {
        /**
         * 语言环境
         */
        locale("locale"),

        userId("userId"),

        token("token", "Authorazation"),
        ;
        String name;

        String alias;

        CachedHeadEunm(String name, String alias) {
            this.name = name;
            this.alias = alias;
        }

        CachedHeadEunm(String name) {
            this.name = name;
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-real-ip");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getLocale() {
        return get(CachedHeadEunm.locale.name);
    }

    static void start(HttpServletRequest request) {
        Integer requestId = seq.incrementAndGet();
        requestIds.set(requestId);
        timeCaches.put(requestId, System.currentTimeMillis());
        ips.set(getIpAddress(request));
        for (CachedHeadEunm paramEunm : CachedHeadEunm.values()) {
            ThreadLocal<String> paramThreadLocal = paramsCache.get(paramEunm.name);
            if (paramThreadLocal == null) {
                paramThreadLocal = new ThreadLocal<>();
                paramsCache.put(paramEunm.name, paramThreadLocal);
            }
            paramThreadLocal.set(getRequestValue(request, paramEunm));
        }
    }

    /**
     * Try to get value from the head, Return value acquired from parameter
     * if value is null.
     *
     * @Description:
     * @Param:
     * @return:
     * @Author: td
     * @Date: 2019/11/26
     */
    private static String getRequestValue(HttpServletRequest request, CachedHeadEunm headEunm) {
        String key = headEunm.name;
        String value = request.getHeader(key);
        if (StringUtils.isEmpty(value)) {
            value = request.getParameter(key);
        }
        if (StringUtils.isEmpty(value) && !StringUtils.isEmpty(headEunm.alias)) {
            value = request.getHeader(headEunm.alias);
            if (StringUtils.isEmpty(value)) {
                value = request.getParameter(headEunm.alias);
            }
        }

        return value;
    }

    static long end() {
        for (CachedHeadEunm paramEunm : CachedHeadEunm.values()) {
            paramsCache.get(paramEunm.name).remove();
        }
        int requestId = requestIds.get();
        long time = System.currentTimeMillis() - timeCaches.get(requestId);
        requestIds.remove();
        timeCaches.remove(requestId);
        ips.remove();
        return time;

    }

    public static Integer getInteger(String name) {
        assert name != null;
        return Integer.parseInt(get(name));
    }

    public static Double getDouble(String name) {
        assert name != null;
        return Double.parseDouble(get(name));
    }

    public static Boolean getBoolean(String name) {
        assert name != null;
        return Boolean.parseBoolean(get(name));
    }

    public static String get(String name) {
        assert name != null;
        ThreadLocal<String> threadLocal = paramsCache.get(name);
        if (threadLocal == null) {
            return null;
        }
        return threadLocal.get();
    }

    public static Integer getRequestId() {
        return requestIds.get();
    }

    public static Long getUserId() {
        String userId = get(CachedHeadEunm.userId.name);
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        return Long.parseLong(userId);
    }


    public static String getToken() {
        return get(CachedHeadEunm.token.name);
    }

    public static String getIp() {
        return ips.get();
    }

    public static String getUserName() {
        String token = getToken();
        if (token == null) {
            return null;
        }
        return TokenUtil.getUserName(token);
    }


}
