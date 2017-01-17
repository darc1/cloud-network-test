package com.vgn.cloud.bandwidth.controller;


import com.vgn.cloud.bandwidth.domain.sampler.IcmpLatencySample;
import com.vgn.cloud.bandwidth.domain.sampler.LatencyTestParams;
import com.vgn.cloud.bandwidth.domain.sampler.ClientParams;
import com.vgn.cloud.bandwidth.domain.sampler.ServerParams;
import com.vgn.cloud.bandwidth.service.sampler.IIcmpSamplerService;
import com.vgn.cloud.bandwidth.service.sampler.IIperf3SamplerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Created by cdoar on 1/8/17.
 */

@RestController
@RequestMapping("/sampler")
public class SamplerController {

    @Autowired
    private IIperf3SamplerService iperf3Service;

    @Autowired
    private IIcmpSamplerService icmpService;

    @RequestMapping(value= "/iperf3/client",method = RequestMethod.POST)
    public String runIperf3Client(@RequestBody ClientParams clientParams) throws IOException {
        return iperf3Service.runIperf3Client(clientParams);
    }

    @RequestMapping(value= "/iperf3/server",method = RequestMethod.POST)
    public String runIperf3Server(@RequestBody ServerParams serverParams) throws IOException {
        iperf3Service.runIperf3Server(serverParams);
        return "ok";
    }

    @RequestMapping(value= "/iperf3/server",method = RequestMethod.DELETE)
    public String killIperf3Server(@RequestBody ServerParams serverParams) throws IOException {
        iperf3Service.killIperf3Server(serverParams);
        return "ok";
    }

    @RequestMapping(value= "/iperf3/server",method = RequestMethod.GET)
    public List<Integer> getIperf3Servers() throws IOException {
        return iperf3Service.getIperf3Servers();

    }

    @RequestMapping(value = "/icmp/run", method = RequestMethod.POST)
    public List<IcmpLatencySample> runIcmpLatencyTest(@RequestBody LatencyTestParams latencyTestParams){
        return icmpService.runLatencyTest(latencyTestParams);
    }


    @RequestMapping(value= "/test",method = RequestMethod.GET)
    public String test() throws IOException {
        return "muhaha it's alive, alive I tell you";
    }
}
