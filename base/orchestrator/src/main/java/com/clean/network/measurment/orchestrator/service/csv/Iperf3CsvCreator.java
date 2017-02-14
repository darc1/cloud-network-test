package com.clean.network.measurment.orchestrator.service.csv;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cdoar on 1/10/17.
 */
@Service
public class Iperf3CsvCreator implements IIperf3CsvCreator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String resultFile = "result.csv";
    private final String indexFile = "index.txt";

    @Override
    public String createFromTestData(String testResultsDir) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<TestResult> resultList = Utils.getObjectsFromDir(testResultsDir, TestResult.class);

        String csvName = Paths.get(testResultsDir, resultFile).toString();
        String indexName = Paths.get(testResultsDir, indexFile).toString();
        FileWriter writer = new FileWriter(csvName);
        FileWriter indexWriter = new FileWriter(indexName);
        Integer index = 1;

        for(TestResult result : resultList){

            Utils.writeLine(indexWriter, result.getClient().getZone() +"->" + result.getServer().getVmInstance().getZone());

            Utils.writeLine(writer, "");
            Utils.writeLine(writer,"client: " + result.getClient().getName() + " " + result.getClient().getZone());
            Utils.writeLine(writer,"server: " + result.getServer().getVmInstance().getName()+ " "
                    + result.getServer().getVmInstance().getZone() +":"+ result.getServer().getListeningPort());
            index += 3;

            JsonNode root = mapper.readTree(result.getTestResults());

            Utils.writeLine(indexWriter, "start: " + index);

            long start = root.path("start").path("timestamp").path("timesecs").asLong();
            Iterator<JsonNode> iterator = root.path("intervals").iterator();

            while (iterator.hasNext()) {
                JsonNode item = iterator.next();
                double elapsed = item.path("sum").path("end").asDouble();
                long bitsPerSec = item.path("sum").path("bits_per_second").asLong();
                long l = start + (long) Math.round(elapsed);
                Utils.writeLine(writer,l + "," + bitsPerSec/(1024.0*1024.0));
                index++;
            }

            Utils.writeLine(indexWriter, "end: " + (index - 1));
        }

        indexWriter.close();
        writer.close();

        return resultFile;


    }



}
