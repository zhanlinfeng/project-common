

package com.deepthink.common.remote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class RemoteServiceRegistrar implements ImportBeanDefinitionRegistrar {
    public RemoteServiceRegistrar() {
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
        String[] basePackages = annoAttrs.getStringArray("basePackages");
        ClasspathRemoteServiceScanner scanner = new ClasspathRemoteServiceScanner(registry);
        scanner.doScan(basePackages);
    }
}
