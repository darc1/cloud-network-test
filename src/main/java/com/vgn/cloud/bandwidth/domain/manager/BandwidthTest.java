package com.vgn.cloud.bandwidth.domain.manager;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vgn.cloud.bandwidth.domain.VMTestInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cdoar on 1/9/17.
 */
public class BandwidthTest {

    private List<VMTestInfo> testVMs;
    private int testDurationInSec;
    private String testId;

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
}
