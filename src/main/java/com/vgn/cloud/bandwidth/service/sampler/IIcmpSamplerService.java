package com.vgn.cloud.bandwidth.service.sampler;

import com.vgn.cloud.bandwidth.domain.sampler.IcmpLatencySample;
import com.vgn.cloud.bandwidth.domain.sampler.LatencyTestParams;

import java.util.List;

/**
 * Created by cdoar on 1/11/17.
 */
public interface IIcmpSamplerService {
    List<IcmpLatencySample> runLatencyTest(LatencyTestParams latencyTestParams);
}
