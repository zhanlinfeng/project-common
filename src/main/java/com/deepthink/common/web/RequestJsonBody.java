package com.deepthink.common.web;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.*;

/**
 * @author td
 * 提取json格式请求体,如果请求体不是json格式，则会解析失败
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.PARAMETER)
public @interface RequestJsonBody {

    /**
     * The name of the request parameter to bind to.
     * @since 4.2
     */
    String name() default "";

    /**
     * Whether the parameter is required.
     * <p>Defaults to {@code true}, leading to an exception being thrown
     * if the parameter is missing in the request. Switch this to
     * {@code false} if you prefer a {@code null} value if the parameter is
     * not present in the request.
     * <p>Alternatively, provide a {@link #defaultValue}, which implicitly
     * sets this flag to {@code false}.
     */
    boolean required() default true;

    /**
     * The default value to use as a fallback when the request parameter is
     * not provided or has an empty value.
     * <p>Supplying a default value implicitly sets {@link #required} to
     * {@code false}.
     */
    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
