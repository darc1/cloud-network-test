package domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by cdoar on 1/9/17.
 */
public class BandwidthTest {

    private List<VMTestInfo> testVMs;
    private int testDurationInSec;
    private boolean udp;
    private int udpBandwidth;
    private String testId;
    private int peers;

    public List<VMTestInfo> getTestVMs() {
        return testVMs;
    }

    public void setTestVMs(List<VMTestInfo> testVMs) {
        this.testVMs = testVMs;
    }

    public int getTestDurationInSec() {
        return testDurationInSec;
    }

    public void setTestDurationInSec(int testDurationInSec) {
        this.testDurationInSec = testDurationInSec;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public boolean isUdp() {
        return udp;
    }

    public void setUdp(boolean udp) {
        this.udp = udp;
    }

    public int getUdpBandwidth() {
        return udpBandwidth;
    }

    public void setUdpBandwidth(int udpBandwidth) {
        this.udpBandwidth = udpBandwidth;
    }

    public int getPeers() {
        return peers;
    }

    public void setPeers(int peers) {
        this.peers = peers;
    }
}
