package com.deepthink.common.mqtt;

import com.deepthink.common.cache.LocalCacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: vending_admin
 * @description:
 * @author: td
 * @create: 2020-03-27 15:13
 **/
@Component
public class MqttContext {
    @Autowired
    private LocalCacher localCacher;

    private final Integer DEFAULT_EXPIRE_TIME = 100000;

    public void setContextId(Long contextId) {
        localCacher.put(contextId.toString(), DEFAULT_EXPIRE_TIME);
    }

    public boolean containContextId(Long contextId) {
        return localCacher.contain(contextId.toString());
    }
}
