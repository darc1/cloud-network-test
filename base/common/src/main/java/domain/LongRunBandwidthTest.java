package domain;

import java.util.List;

/**
 * Created by root on 1/24/17.
 */
public class LongRunBandwidthTest {

    private BandwidthTest bandwidthTest;

    private int numRuns;
    private int sleepIntervalBetweenRuns;

    public BandwidthTest getBandwidthTest() {
        return bandwidthTest;
    }

    public void setBandwidthTest(BandwidthTest bandwidthTest) {
        this.bandwidthTest = bandwidthTest;
    }

    public int getSleepIntervalBetweenRuns() {
        return sleepIntervalBetweenRuns;
    }

    public void setSleepIntervalBetweenRuns(int sleepIntervalBetweenRuns) {
        this.sleepIntervalBetweenRuns = sleepIntervalBetweenRuns;
    }

    public int getNumRuns() {
        return numRuns;
    }

    public void setNumRuns(int numRuns) {
        this.numRuns = numRuns;
    }
}
