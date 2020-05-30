//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.deepthink.common.remote;

import com.deepthink.common.util.HttpUtils;
import io.netty.handler.codec.http.HttpUtil;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class HttpInvocationHandler implements InvocationHandler {
    private ConcurrentHashMap<String, MethodHttpMetaData> metaData = new ConcurrentHashMap();

    public HttpInvocationHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        MethodHttpMetaData methodHttpMetaData = (MethodHttpMetaData) this.metaData.get(name);
        if (methodHttpMetaData == null) {
            methodHttpMetaData = new MethodHttpMetaData(method);
            this.metaData.putIfAbsent(name, methodHttpMetaData);
        }
        doHttpRequest(methodHttpMetaData, args);
        return null;
    }

    private void doHttpRequest(MethodHttpMetaData methodHttpMetaData, Object[] args) {
        RequestMethod requestMethod = methodHttpMetaData.getRequestMethod();

//        switch (requestMethod){
//            case GET:
//                HttpUtils.executeGet();
//                break;
//            case POST:
//                HttpUtils.executePost();
//            default:
//                break;
//        }
    }
}
