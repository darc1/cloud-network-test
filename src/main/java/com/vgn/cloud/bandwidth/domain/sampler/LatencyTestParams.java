package com.vgn.cloud.bandwidth.domain.sampler;

/**
 * Created by cdoar on 1/11/17.
 */
public class LatencyTestParams {


    private int numberOfRuns;
    private String destinationIp;
    private long waitIntervalBetweenSamples;

    public int getNumberOfRuns() {

        return numberOfRuns;
    }

    public void setNumberOfRuns(int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public long getWaitIntervalBetweenSamples() {
        return waitIntervalBetweenSamples;
    }

    public void setWaitIntervalBetweenSamples(long waitIntervalBetweenSamples) {
        this.waitIntervalBetweenSamples = waitIntervalBetweenSamples;
    }

}
