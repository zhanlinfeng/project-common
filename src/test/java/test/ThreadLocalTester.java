package test;

import com.alibaba.fastjson.TypeReference;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @program: admin_server
 * @description: ThreadLocal测试类
 * @author: td
 * @create: 2019-11-28 20:17
 **/
public class ThreadLocalTester extends BaseWebTest{


    @Test
    public void baseTest() throws Exception {

        mockMvc.perform(post("/test/ThreadLocalTestLocale").
                accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).
                header("locale", "chinese")).andExpect(jsonPath("data").value("chinese"));

        mockMvc.perform(post("/test/ThreadLocalTestUserId").
                accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).
                param("userId", "999")).andExpect(jsonPath("data").value(999L));
    }

    public static void main(String[] args) {

        TypeReference typeReference = new TypeReference<List<List<String>>>() {};
        TypeVariable<? extends Class<? extends TypeReference>>[] typeParameters = typeReference.getClass().getTypeParameters();

        Type genericSuperclass = typeReference.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        // 获得父接口的泛型信息
        Type genericType = parameterizedType.getActualTypeArguments()[0];
    }
}
