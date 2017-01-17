package com.vgn.cloud.bandwidth.service.sampler;

import com.vgn.cloud.bandwidth.domain.sampler.ClientParams;
import com.vgn.cloud.bandwidth.domain.sampler.ServerParams;

import java.io.IOException;
import java.util.List;

/**
 * Created by cdoar on 1/8/17.
 */
public interface IIperf3SamplerService {

    String runIperf3Client(ClientParams clientParams) throws IOException;
    void runIperf3Server(ServerParams serverParams) throws IOException;
    void killIperf3Server(ServerParams serverParams) throws IOException;
    List<Integer> getIperf3Servers();
}
