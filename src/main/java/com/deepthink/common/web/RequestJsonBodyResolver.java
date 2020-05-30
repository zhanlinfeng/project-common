package com.deepthink.common.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.deepthink.common.util.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @program: vending_admin
 * @description: Get parameter from json type request body
 * @author: td
 * @create: 2020-04-09 17:10
 **/
public class RequestJsonBodyResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestJsonBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        // 解析请求体
        JSONObject jsonBody = getJsonRequestBody(request);
        RequestJsonBody annotation = parameter.getParameterAnnotation(RequestJsonBody.class);
        // 获取默认值
        Object defaultValue = annotation.defaultValue();
        String name = annotation.name();
        if (StringUtils.isEmpty(name)) {
            name = parameter.getParameterName();
        }
        String underLineName = camelToUnderline(name);
        // 获取参数类型
        Class<?> parameterType = parameter.getParameterType();
        Object value = null;
        if (jsonBody.containsKey(name)) {
            value = jsonBody.getObject(name, parameterType);
        // 检查下划线格式的的参数是否存在
        } else if (jsonBody.containsKey(underLineName)) {
            value = jsonBody.getObject(underLineName, parameterType);
        } else {
            if (annotation.required()) {
                throw new MissingServletBodyParamterException(name, parameterType.toString());
            }
        }
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    private JSONObject getJsonRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader bodyReader = request.getReader();
        StringBuilder body = new StringBuilder();
        String line = null;
        while ((line = bodyReader.readLine()) != null) {
            body.append(line);
        }
        ParserConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        return JSON.parseObject(body.toString());
    }


    /**
     * 驼峰命名转为下划线命名
     *
     * @param str
     * @return
     */
    private String camelToUnderline(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(underLine);
                c = Character.toLowerCase(c);
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private String underLine = "_";
}
