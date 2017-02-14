package com.clean.network.measurment.orchestrator.controller;


import cloud.CloudProvider;
import com.clean.network.measurment.orchestrator.cloud.aws.AWSRegions;
import com.clean.network.measurment.orchestrator.cloud.google.GoogleRegions;
import com.clean.network.measurment.orchestrator.service.IManagerService;
import domain.*;
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

    String aws_cali = "aws-california-";
    String aws_mum = "aws-mumbai-";

    @Autowired
    private IManagerService service;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    private String Test() throws IOException, GeneralSecurityException, InterruptedException {

        LongRunBandwidthTest bandwidthTest = new LongRunBandwidthTest();
        bandwidthTest.setNumRuns(4);
        bandwidthTest.setSleepIntervalBetweenRuns(0);

        BandwidthTest btest = new BandwidthTest();


//        btest.setTestId("8-to-8");
//        btest.setTestVMs(create_mum_cali_pairs(8,false));
//        service.runIperf3Test(btest);
//        service.runIperf3Test(btest);
//        service.runIperf3Test(btest);


//        btest.setUdp(true);
//        btest.setTestId("1-to-1");
//        btest.setTestVMs(create_mum_cali_pairs(1,false));
//        btest.setTestDurationInSec(120);
//        service.runIperf3Test(btest);
//        service.runIperf3Test(btest);
//        service.runIperf3Test(btest);
//        service.runIperf3Test(btest);
//        service.runIperf3Test(btest);
//        service.runIperf3Test(btest);


        btest.setTestId("4-to-4");
//        btest.setTestVMs(create_mum_cali_pairs(4,true));
//        service.runIperf3Test(btest);

        btest.setTestVMs(create_mum_cali_pairs(4,false));
        service.runIperf3Test(btest);
        service.runIperf3Test(btest);
        service.runIperf3Test(btest);
        service.runIperf3Test(btest);

        btest.setTestId("2-to-2");
        btest.setTestVMs(create_mum_cali_pairs(2,false));
        service.runIperf3Test(btest);
        service.runIperf3Test(btest);
        service.runIperf3Test(btest);




        return "muhaha it's alive, alive I tell you";
    }

    private VMTestInfo getMumVmInfo(int index, boolean createNew, int targetIndex) {

        return getVmTestInfo(aws_mum+ index,
                    CloudProvider.AWS,
                    AWSRegions.AP_SOUTH_1,
                    aws_cali+ targetIndex,
                    AWSRegions.US_WEST_1, createNew
            );
    }

    private VMTestInfo getCaliVmTestInfo(int index, boolean createNew, int targetIndex) {

        return getVmTestInfo(aws_cali + index,
                    CloudProvider.AWS,
                    AWSRegions.US_WEST_1,
                    aws_mum+ targetIndex,
                    AWSRegions.AP_SOUTH_1, createNew);
    }

    private List<VMTestInfo> create_mum_cali_pairs(int nums, boolean createNew){

        ArrayList<VMTestInfo> r = new ArrayList<>();
        for(int i = 1; i <= nums; i++){

            r.add(getCaliVmTestInfo(i, createNew, i));
            r.add(getMumVmInfo(i, createNew, i));
        }

        return r;
    }

    private static VMTestInfo getVmTestInfo(String vmName, CloudProvider vmProvider, String vmRegion, String targetName, String targetRegion, boolean createNew) {
        VMTestInfo vm = new VMTestInfo();
        vm.setCloudProvider(vmProvider);
        vm.setRegion(vmRegion);
        vm.setName(vmName);
        vm.setNumberOfServers(1);
        vm.setCreateNew(createNew);
        vm.setMachineType("m4.xlarge");
        VMTestTarget target = new VMTestTarget();
        target.setName(targetName);
        target.setRegion(targetRegion);
        target.setConnections(1);
        ArrayList<VMTestTarget> targets = new ArrayList<>();
        targets.add(target);
        vm.setTargets(targets);
        return vm;
    }


    @RequestMapping(value = "/run/bandwidth", method = RequestMethod.POST)
    private String runBandwidthTest(@RequestBody BandwidthTest bandwidthTest){

        return service.runIperf3Test(bandwidthTest);
    }

    @RequestMapping(value = "/run/long-bandwidth", method = RequestMethod.POST)
    private String runLongBandwidthTest(@RequestBody LongRunBandwidthTest bandwidthTest){

        return service.runLongIperf3Test(bandwidthTest);
    }

    @RequestMapping(value = "/run/latency", method = RequestMethod.POST)
    private String runLatencyTest(@RequestBody LatencyTest latencyTest){

        return service.runPingLatencyTest(latencyTest);
    }


}
