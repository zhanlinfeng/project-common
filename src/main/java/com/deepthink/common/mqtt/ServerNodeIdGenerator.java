package com.deepthink.common.mqtt;


import java.util.UUID;

public class ServerNodeIdGenerator {
    private static final String serverNodeId = UUID.randomUUID().toString();

    public static String getServerNodeId() {
        return serverNodeId;
    }
}
