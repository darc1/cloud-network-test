package com.vgn.cloud.bandwidth.service.manager;

import com.vgn.cloud.bandwidth.domain.manager.BandwidthTest;
import com.vgn.cloud.bandwidth.domain.manager.LatencyTest;
import com.vgn.cloud.bandwidth.service.manager.tests.IIcmpLatencyTest;
import com.vgn.cloud.bandwidth.service.manager.tests.IIperf3Test;
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

    @Override
    public String runIperf3Test(BandwidthTest bandwidthTest) {
        return iperf3Test.runTest(bandwidthTest);
    }

    @Override
    public String runPingLatencyTest(LatencyTest latencyTestParams) {
        return icmpLatencyTest.runTest(latencyTestParams);
    }

}
