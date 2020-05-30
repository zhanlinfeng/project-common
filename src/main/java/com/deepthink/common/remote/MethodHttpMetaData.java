
package com.deepthink.common.remote;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.data.mapping.model.IllegalMappingException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping
public class MethodHttpMetaData {
    private RequestMethod requestMethod;
    private String path;
    private MethodHttpMetaData.HttpParamType[] httpParamTypes;
    private Class<?>[] paramType;
    private Class<?> returnType;

    public RequestMethod getRequestMethod() {
        return this.requestMethod;
    }

    public String getPath() {
        return this.path;
    }

    public MethodHttpMetaData.HttpParamType[] getHttpParamTypes() {
        return this.httpParamTypes;
    }

    public Class<?>[] getParamType() {
        return this.paramType;
    }

    public MethodHttpMetaData(Method method) {
        RequestMapping annotation = (RequestMapping) method.getAnnotation(RequestMapping.class);
        if (annotation == null) {
            throw new IllegalMappingException("没有指定请求路径");
        } else {
            this.path = annotation.name();
            RequestMethod[] requestMethods = annotation.method();
            if (requestMethods.length == 0) {
                this.requestMethod = RequestMethod.GET;
            } else {
                this.requestMethod = requestMethods[0];
            }

            this.paramType = method.getParameterTypes();
            this.httpParamTypes = new MethodHttpMetaData.HttpParamType[this.paramType.length];
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            for (int i = 0; i < parameterAnnotations.length; ++i) {
                Annotation[] annotations = parameterAnnotations[i];

                for (Annotation paramAnnotation : annotations) {
                    if (paramAnnotation.annotationType() == RequestParam.class) {
                        this.httpParamTypes[i] = MethodHttpMetaData.HttpParamType.REQUEST_PARAM;
                    } else if (paramAnnotation.annotationType() == RequestBody.class) {
                        this.httpParamTypes[i] = MethodHttpMetaData.HttpParamType.REQUEST_BODY;
                    } else if (paramAnnotation.annotationType() == RequestHeader.class) {
                        this.httpParamTypes[i] = MethodHttpMetaData.HttpParamType.HEAD;
                    }
                }

                if (this.httpParamTypes[i] == null) {
                    this.httpParamTypes[i] = MethodHttpMetaData.HttpParamType.REQUEST_PARAM;
                }
            }

        }
    }

    enum HttpParamType {
        /**
         * 头
         */
        HEAD,
        /**
         * 请求参数
         */
        REQUEST_PARAM,
        /**
         * 请求体
         */
        REQUEST_BODY;

        private HttpParamType() {
        }
    }
}
