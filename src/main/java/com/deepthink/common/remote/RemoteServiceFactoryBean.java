

package com.deepthink.common.remote;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class RemoteServiceFactoryBean<T> implements FactoryBean, EnvironmentAware {
    private static final RemoteServiceProxyCreator proxyCreator = new RemoteServiceProxyCreator();
    private Class<T> remoteServiceInterface;

    public RemoteServiceFactoryBean(Class<T> remoteServiceInterface) {
        this.remoteServiceInterface = remoteServiceInterface;
    }

    @Override
    public T getObject() throws Exception {
        return null;
    }

    @Override
    public Class<T> getObjectType() {
        return this.remoteServiceInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    @Override
    public void setEnvironment(Environment environment) {
    }
}
