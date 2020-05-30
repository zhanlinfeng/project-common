
package com.deepthink.common.remote;

import java.lang.reflect.Proxy;

class RemoteServiceProxyCreator {
    RemoteServiceProxyCreator() {
    }

    public <T> T proxy(Class<T> remoteServiceInterface) {
        HttpInvocationHandler handler = new HttpInvocationHandler();
        T remoteServiceProxy = (T) Proxy.newProxyInstance(remoteServiceInterface.getClassLoader(), new Class[]{remoteServiceInterface}, handler);
        return remoteServiceProxy;
    }
}
