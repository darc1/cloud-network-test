package com.vgn.cloud.bandwidth.controller;

import com.vgn.cloud.bandwidth.cloud.aws.AWSRegions;
import com.vgn.cloud.bandwidth.cloud.aws.AmazonWebServices;
import com.vgn.cloud.bandwidth.domain.VMTestInfo;
import com.vgn.cloud.bandwidth.domain.manager.BandwidthTest;
import com.vgn.cloud.bandwidth.domain.manager.LatencyTest;
import com.vgn.cloud.bandwidth.service.manager.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdoar on 1/8/17.
 */

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private AmazonWebServices aws;

    @Autowired
    private IManagerService service;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    private String Test() throws IOException, GeneralSecurityException, InterruptedException {
        return "muhaha it's alive, alive I tell you";
    }


    @RequestMapping(value = "/run/bandwidth", method = RequestMethod.POST)
    private String runBandwidthTest(@RequestBody BandwidthTest bandwidthTest){

        return service.runIperf3Test(bandwidthTest);
    }

    @RequestMapping(value = "/run/latency", method = RequestMethod.POST)
    private String runLatencyTest(@RequestBody LatencyTest latencyTest){

        return service.runPingLatencyTest(latencyTest);
    }
}
