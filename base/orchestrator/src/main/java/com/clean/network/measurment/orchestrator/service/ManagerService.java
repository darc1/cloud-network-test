package com.clean.network.measurment.orchestrator.service;


import com.clean.network.measurment.orchestrator.service.tests.IIcmpLatencyTest;
import com.clean.network.measurment.orchestrator.service.tests.IIperf3Test;
import com.clean.network.measurment.orchestrator.service.tests.ILongRunIperf3Test;
import domain.BandwidthTest;
import domain.LatencyTest;
import domain.LongRunBandwidthTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by cdoar on 1/8/17.
 */
@Service
public class ManagerService implements IManagerService {

    @Autowired
    private IIperf3Test iperf3Test;

    @Autowired
    private IIcmpLatencyTest icmpLatencyTest;

    @Autowired
    private ILongRunIperf3Test longRunIperf3Test;

    @Override
    public String runIperf3Test(BandwidthTest bandwidthTest) {
        return iperf3Test.runTest(bandwidthTest);
    }

    @Override
    public String runLongIperf3Test(LongRunBandwidthTest bandwidthTest) {
        return longRunIperf3Test.runTest(bandwidthTest);
    }

    @Override
    public String runPingLatencyTest(LatencyTest latencyTestParams) {
        return icmpLatencyTest.runTest(latencyTestParams);
    }

}
