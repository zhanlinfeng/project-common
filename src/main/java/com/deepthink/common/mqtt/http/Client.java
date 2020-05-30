package com.deepthink.common.mqtt.http;

/**
 * @author td
 */
public class Client {
    private String clientId;

    private String userName;

    private String ipaddress;

    private Integer port;

    private boolean cleanSess;

    private Integer protoVer;

    private Integer keepalive;

    private String connectedAt;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public boolean isCleanSess() {
        return cleanSess;
    }

    public void setCleanSess(boolean cleanSess) {
        this.cleanSess = cleanSess;
    }

    public Integer getProtoVer() {
        return protoVer;
    }

    public void setProtoVer(Integer protoVer) {
        this.protoVer = protoVer;
    }

    public Integer getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(Integer keepalive) {
        this.keepalive = keepalive;
    }

    public String getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(String connectedAt) {
        this.connectedAt = connectedAt;
    }
}
