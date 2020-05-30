package util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @program: admin_server
 * @description: 反射工具
 * @author: td
 * @create: 2019-11-28 19:51
 **/
public class ReflectUtils {
    private ReflectUtils() {

    }

    public static <T> T getFieldValueForce(Class<T> fieldType, String name, Object target) {
        Field field = ReflectionUtils.findField(target.getClass(), name);
        field.setAccessible(true);
        return (T) ReflectionUtils.getField(field, target);
    }
}
