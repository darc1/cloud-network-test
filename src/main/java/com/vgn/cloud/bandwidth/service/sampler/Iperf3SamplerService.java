package com.vgn.cloud.bandwidth.service.sampler;

import com.vgn.cloud.bandwidth.domain.sampler.ClientParams;
import com.vgn.cloud.bandwidth.domain.sampler.ServerParams;
import com.vgn.cloud.bandwidth.domain.sampler.ServerProcess;
import com.vgn.cloud.bandwidth.service.ProcessRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cdoar on 1/8/17.
 */
@Service
public class Iperf3SamplerService implements IIperf3SamplerService {


    private String IPERF3 = "iperf3";
    private List<ServerProcess> serverProcessList = new ArrayList<>();

    @Override
    public String runIperf3Client(ClientParams clientParams) throws IOException {

        ProcessRunner runner = new ProcessRunner();

        List<String> processArgs = createIperfClientArgs(clientParams);
        return runner.RunProcessAndGetOutput(IPERF3, processArgs);
    }

    @Override
    public void runIperf3Server(ServerParams serverParams) throws IOException {
        ProcessRunner runner = new ProcessRunner();

        List<String> processArgs = createIperfServerArgs(serverParams);
        Process serverProcess = runner.createProcess(IPERF3, processArgs);

        serverProcessList.add(new ServerProcess(serverProcess,serverParams.getPort()));
    }

    @Override
    public void killIperf3Server(ServerParams serverParams) throws IOException {

        if(serverProcessList == null) return;

        for(ServerProcess serverProcess : serverProcessList){
            if(serverProcess.getPort() == serverParams.getPort()){
                serverProcess.getProcess().destroy();
            }
        }
    }

    @Override
    public List<Integer> getIperf3Servers() {

        if(serverProcessList == null) return new ArrayList<>();
        return serverProcessList.stream().map(x -> x.getPort()).collect(Collectors.toList());
    }

    private List<String> createIperfServerArgs(ServerParams serverParams) {
        ArrayList<String> processParams = new ArrayList<>();
        processParams.add("--server");

        if(serverParams.getPort() > 0){
            processParams.add("--port");
            processParams.add(String.valueOf(serverParams.getPort()));
        }

        processParams.add("--json");

        return processParams;
    }

    private List<String> createIperfClientArgs(ClientParams clientParams) {
        ArrayList<String> processParams = new ArrayList<>();

        processParams.add("--client");
        processParams.add(clientParams.getClient());

        processParams.add("--json");

        if(clientParams.isUdp()){
            processParams.add("--udp");
            processParams.add("--bandwidth");
            processParams.add("10G");
        }

        if (clientParams.getTestLengthInSec() > 0){
            processParams.add("--time");
            processParams.add(String.valueOf(clientParams.getTestLengthInSec()));
        }

        if (clientParams.getPort() > 0){
            processParams.add("--port");
            processParams.add(String.valueOf(clientParams.getPort()));
        }

        return processParams;
    }
}
