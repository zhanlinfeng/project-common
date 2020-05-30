package com.deepthink.common.locale;


import com.deepthink.common.web.RequestThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: data_server
 * @description:国际化消息
 * @author: td
 * @create: 2019-11-22 10:51
 **/
@Component
public class I18nMessage {

    private static final Logger logger = LoggerFactory.getLogger(I18nMessage.class);

    @Value("${i18nMessage.default-locale:zh}")
    private static String defaultLocale = "zh";

    private static ConcurrentHashMap<String, ResourceBundle> resourceBundleMap;
    /**
     *获得国际化的消息，当前语言环境是请求线程本地存储 {@link RequestThreadLocal} 确定的,
     * 不在请求主线程的请使用其同名重载方法 {@link #getMsg(String, String)}}
     * @Param:
     * @return:
     * @Author: td
     * @Date: 2019/11/22
     */
    public static String getMsg(String key) {
        String locale = getLocale();
        return getMsg(key, locale);
    }

    private static String getLocale() {
        String locale = RequestThreadLocal.getLocale();
        if (locale == null) {
            locale = defaultLocale;
        }
        return locale;
    }

    public static String getMsg(String key, String locale) {
        if (locale == null) {
            locale = defaultLocale;
            logger.warn("Using default locale {} as locale not specified", defaultLocale);
        }
        ResourceBundle resourceBundle = resourceBundleMap.get(locale);

        if (resourceBundle == null) {
            logger.warn("Can't find resource needed, check configuration or locale accquired! ");
            return null;
        }
        String str = null;
        try {
            resourceBundle.getString(key);
        } catch (MissingResourceException mre) {
            logger.error("为找到相关资源！", mre);
            return null;
        }

        String utf8String = null;
        try {
            utf8String = new String(str.getBytes("ISO8859-1"), "GBK");
        } catch (UnsupportedEncodingException e) {
            logger.error("字符编码转化异常！", e);
        }
        return utf8String ;
    }
    @PostConstruct
    public void init() {
        resourceBundleMap = new ConcurrentHashMap<>(2);
        try {
            resourceBundleMap.put("Chinese", ResourceBundle.getBundle("locale.Chinese", Locale.CHINESE));
            resourceBundleMap.put("English", ResourceBundle.getBundle("locale.English", Locale.ENGLISH));
        } catch (Exception e) {
            logger.warn("国际消息组件未能正常初始化，msg：{}", e.getMessage());
        }

    }

}
