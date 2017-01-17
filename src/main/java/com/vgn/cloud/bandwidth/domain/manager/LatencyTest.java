package com.vgn.cloud.bandwidth.domain.manager;

import com.vgn.cloud.bandwidth.domain.VMTestInfo;
import com.vgn.cloud.bandwidth.domain.sampler.LatencyTestParams;

import java.util.List;

public class LatencyTest {

    private String testId;
    private List<VMTestInfo> testVMs;
    private LatencyTestParams testParams;


    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }


    public List<VMTestInfo> getTestVMs() {
        return testVMs;
    }

    public void setTestVMs(List<VMTestInfo> testVMs) {
        this.testVMs = testVMs;
    }

    public LatencyTestParams getTestParams() {
        return testParams;
    }

    public void setTestParams(LatencyTestParams testParams) {
        this.testParams = testParams;
    }
}
