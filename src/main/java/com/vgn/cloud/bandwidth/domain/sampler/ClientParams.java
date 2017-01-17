package com.vgn.cloud.bandwidth.domain.sampler;

/**
 * Created by cdoar on 1/8/17.
 */
public class ClientParams {

    private String client;
    private int testLengthInSec;
    private boolean udp;
    private int port;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getTestLengthInSec() {
        return testLengthInSec;
    }

    public void setTestLengthInSec(int testLengthInSec) {
        this.testLengthInSec = testLengthInSec;
    }

    public boolean isUdp() {
        return udp;
    }

    public void setUdp(boolean udp) {
        this.udp = udp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
