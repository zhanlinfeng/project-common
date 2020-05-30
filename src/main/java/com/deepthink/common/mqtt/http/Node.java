package com.deepthink.common.mqtt.http;

/**
 * @author Administrator
 */
public class Node {
    private String name;

    private String version;

    private String sysdescr;

    private String uptime;

    private String datetime;

    private String otpRelease;

    private String nodeStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSysdescr() {
        return sysdescr;
    }

    public void setSysdescr(String sysdescr) {
        this.sysdescr = sysdescr;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getOtpRelease() {
        return otpRelease;
    }

    public void setOtpRelease(String otpRelease) {
        this.otpRelease = otpRelease;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }
}