package com.clean.network.measurement.service;


import domain.IcmpLatencySample;
import domain.LatencyTestParams;

import java.util.List;

/**
 * Created by cdoar on 1/11/17.
 */
public interface IIcmpSamplerService {
    List<IcmpLatencySample> runLatencyTest(LatencyTestParams latencyTestParams);
}
