package com.deepthink.common.mqtt.http;

import java.util.List;

/**
 * @author Administrator
 */
public interface MqttHttpClient {
    /**
     * 获取节点信息
     * @return 节点信息
     */
    List<Node> getNodes();


    /**
     * 获取节点客户端信息
     * @return 节点客户端信息
     */
    List<Client> getClients(Node node);


}
