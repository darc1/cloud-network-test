package com.clean.network.measurment.orchestrator.service;


import domain.BandwidthTest;
import domain.LatencyTest;
import domain.LongRunBandwidthTest;

/**
 * Created by cdoar on 1/8/17.
 */
public interface IManagerService {

    String runIperf3Test(BandwidthTest bandwidthTest);

    String runLongIperf3Test(LongRunBandwidthTest bandwidthTest);

    String runPingLatencyTest(LatencyTest latencyTestParams);
}
