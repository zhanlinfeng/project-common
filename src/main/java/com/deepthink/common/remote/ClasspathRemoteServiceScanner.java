

package com.deepthink.common.remote;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

public class ClasspathRemoteServiceScanner extends ClassPathBeanDefinitionScanner {
    private final Class<?> remoteServiceFactoryBeanClass = RemoteServiceFactoryBean.class;

    public ClasspathRemoteServiceScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            this.logger.warn("No remote service was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            this.processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition = null;
        Iterator beanDefinitionIterator = beanDefinitions.iterator();

        while (beanDefinitionIterator.hasNext()) {
            BeanDefinitionHolder holder = (BeanDefinitionHolder) beanDefinitionIterator.next();
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            definition.setBeanClass(this.remoteServiceFactoryBeanClass);
        }

    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().hasAnnotation(RemoteService.class.getName());
    }
}
