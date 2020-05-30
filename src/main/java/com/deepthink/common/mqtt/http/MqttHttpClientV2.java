package com.deepthink.common.mqtt.http;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import com.deepthink.common.util.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author td
 */
//@Component("MqttHttpClientV2")
public class MqttHttpClientV2 implements MqttHttpClient{

    static {
        ParserConfig.getGlobalInstance().propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

    @Value("${mqtt.http-api-address:}")
    private String httpApiAddress;

    @Value("${mqtt.username:}")
    private String username;

    @Value("${mqtt.password:}")
    private String password;



    private final static String GET_NODES_INTERFACE = "/api/v2/management/nodes";

    private final static String NODE_NAME_PLACEHOLDER = "{nodeName}";

    private final static String GET_CLIENTS_INTERFACE = "/api/v2/nodes/" + NODE_NAME_PLACEHOLDER + "/clients";



    @Override
    public List<Node> getNodes() {
        String ret = HttpUtils.executeAuthorizationGet(httpApiAddress + GET_NODES_INTERFACE,
                username, password, null);
        JSONObject jsonRet = JSONObject.parseObject(ret);
        MqttHttpResult<List<Node>>  result = jsonRet.toJavaObject(new TypeReference<MqttHttpResult<List<Node>>>() {});
        if (result != null) {
            return result.getResult();
        }
        return null;
    }

    @Override
    public List<Client> getClients(Node node) {
        if (!isRunning(node)) {
            return new ArrayList<>();
        }
        String url = getClientUrl(node);
        String ret = HttpUtils.executeAuthorizationGet(url,
                username, password, null);
        JSONObject jsonRet = JSONObject.parseObject(ret);
        MqttHttpResult<Clients>  result = jsonRet.toJavaObject(new TypeReference<MqttHttpResult<Clients>>(){});
        if (result != null) {
            return result.getResult().getObjects();
        }
        return null;
    }

    private boolean isRunning(Node node) {
        return "Running".equals(node.getNodeStatus());
    }

    private String getClientUrl(Node node) {
        return httpApiAddress + GET_CLIENTS_INTERFACE.replace(NODE_NAME_PLACEHOLDER, node.getName());
    }

}
