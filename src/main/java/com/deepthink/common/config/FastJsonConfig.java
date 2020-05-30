package com.deepthink.common.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: industry-train-platform
 * @description: alibaba fastjson配置
 * @author: td
 * @create: 2019-10-16 15:05
 **/
@Configuration
public class FastJsonConfig {
    @Bean
    public FastJsonHttpMessageConverter getFastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converter.setFeatures(SerializerFeature.SkipTransientField,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteEnumUsingName,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect
        );
        // 解决前端Long型精度丢失的问题
        converter.addSerializeFilter(new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                if (value != null) {
                    if (value instanceof Long) {
                        Long longValue = (Long) value;
                        if (longValue > Integer.MAX_VALUE) {
                            return value.toString();
                        }
                    }
                }
                return value;
            }
        });
        return converter;
    }

    @Bean
    public FieldRetrievingFactoryBean getFieldRetrievingFactoryBean() {
        FieldRetrievingFactoryBean fieldRetrievingFactoryBean = new FieldRetrievingFactoryBean();
        fieldRetrievingFactoryBean.setStaticField("com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect");
        return fieldRetrievingFactoryBean;
    }
}
