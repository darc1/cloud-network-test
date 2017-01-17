package com.vgn.cloud.bandwidth.service.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.repackaged.com.google.common.annotations.Beta;
import com.vgn.cloud.bandwidth.common.Utils;
import com.vgn.cloud.bandwidth.domain.TestResult;
import com.vgn.cloud.bandwidth.domain.manager.LatencyTestResult;
import com.vgn.cloud.bandwidth.domain.sampler.IcmpLatencySample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by root on 1/12/17.
 */
@Service
public class IcmpCsvCreator implements IIcmpCsvCreator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String resultFile = "result.csv";
    private final String indexFile = "index.txt";

    @Override
    public String createFromTestData(String testResultsDir) throws IOException {

        List<LatencyTestResult> testResults = Utils.getObjectsFromDir(testResultsDir, LatencyTestResult.class);
        String csvName = Paths.get(testResultsDir, resultFile).toString();
        String IndexName = Paths.get(testResultsDir, indexFile).toString();
        FileWriter writer = new FileWriter(csvName);
        FileWriter indexWriter = new FileWriter(csvName);
        Integer index = 1;
        for(LatencyTestResult clientSamples : testResults){

            Utils.writeLine(indexWriter, clientSamples.getSourceZone() + "->" + clientSamples.getTargetZone());
            Utils.writeLine(writer,"");
            Utils.writeLine(writer, clientSamples.getSource() + " " + clientSamples.getSourceZone());
            Utils.writeLine(writer, clientSamples.getTarget() + " " + clientSamples.getTargetZone());
            index += 3;
            Utils.writeLine(indexWriter,"start: " + index);
            for(IcmpLatencySample sample : clientSamples.getSamples()){
                if(sample.isSuccessFlag()){
                    Utils.writeLine(writer, String.valueOf(sample.getRtt()));
                }else {
                    Utils.writeLine(writer, sample.getErrorMessage());
                }
                index++;
            }
            Utils.writeLine(indexWriter, "end: " + (index -1));
        }

        writer.close();
        indexWriter.close();
        return csvName;
    }


}
