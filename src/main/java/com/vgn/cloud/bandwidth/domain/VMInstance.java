package com.vgn.cloud.bandwidth.domain;

/**
 * Created by cdoar on 1/8/17.
 */
public class VMInstance {

    private String publicIp;
    private String privateIp;
    private String zone;
    private String name;
    private VMTestInfo testInfo;

    public VMInstance() {
    }

    public VMTestInfo getTestInfo() {
        return testInfo;
    }

    public void setTestInfo(VMTestInfo testInfo) {
        this.testInfo = testInfo;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
