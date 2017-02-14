package com.clean.network.measurement.service;


import domain.IcmpLatencySample;
import domain.LatencyTestParams;
import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdoar on 1/11/17.
 */
@Service
public class IcmpSamplerService implements IIcmpSamplerService {

    @Override
    public List<IcmpLatencySample> runLatencyTest(LatencyTestParams latencyTestParams) {

        List<IcmpLatencySample> samples = new ArrayList<>(latencyTestParams.getNumberOfRuns());
        IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
        request.setHost(latencyTestParams.getDestinationIp());

        for (int i = 0; i < latencyTestParams.getNumberOfRuns(); i++){

            IcmpPingResponse response = IcmpPingUtil.executePingRequest(request);
            IcmpLatencySample sample = new IcmpLatencySample();
            sample.setTargetIp(response.getHost());
            sample.setDuration(response.getDuration());
            sample.setErrorMessage(response.getErrorMessage());
            sample.setRtt(response.getRtt());
            sample.setSize(response.getSize());
            sample.setSuccessFlag(response.getSuccessFlag());
            sample.setTimeoutFlag(response.getTimeoutFlag());
            sample.setTtl(response.getTtl());
            samples.add(sample);

            try {
                Thread.sleep(latencyTestParams.getWaitIntervalBetweenSamples());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return samples;
    }
}
