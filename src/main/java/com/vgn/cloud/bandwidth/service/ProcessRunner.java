package com.vgn.cloud.bandwidth.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdoar on 1/8/17.
 */
public class ProcessRunner {


    public String RunProcessAndGetOutput(String processName, List<String> processArgs) throws IOException {


        Process process = createProcess(processName, processArgs);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String input = null;
        StringBuilder result = new StringBuilder();
        while ((input = stdInput.readLine()) != null){
            result.append(input);
        }

        return result.toString();
    }

    public Process createProcess(String processName, List<String> processArgs) throws IOException {
        ArrayList<String> processParams = createProcessParams(processName, processArgs);

        ProcessBuilder processBuilder = new ProcessBuilder(processParams);

        return processBuilder.start();
    }

    private ArrayList<String> createProcessParams(String processName, List<String> processArgs) {
        ArrayList<String> processCommand = new ArrayList<>();
        processCommand.add(processName);
        for (String param: processArgs) {
            processCommand.add(param);

        }
        return processCommand;
    }

}
