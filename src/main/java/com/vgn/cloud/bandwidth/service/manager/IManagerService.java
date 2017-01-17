package com.vgn.cloud.bandwidth.service.manager;

import com.vgn.cloud.bandwidth.domain.manager.BandwidthTest;
import com.vgn.cloud.bandwidth.domain.manager.LatencyTest;

/**
 * Created by cdoar on 1/8/17.
 */
public interface IManagerService {

    String runIperf3Test(BandwidthTest bandwidthTest);

    String runPingLatencyTest(LatencyTest latencyTestParams);
}
