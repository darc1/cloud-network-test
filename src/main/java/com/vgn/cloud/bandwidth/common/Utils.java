package com.vgn.cloud.bandwidth.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vgn.cloud.bandwidth.domain.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by root on 1/12/17.
 */
public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static String createTestDir(String testId, String testType) throws IOException {

        Path iperf3 = Paths.get(testType);
        if (!Files.exists(iperf3)) {
            Files.createDirectory(iperf3);
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String testDir = testId + "_" + timeStamp;
        Files.createDirectory(Paths.get(testDir));
        return testDir;
    }

    public static <T> void writeObjectToFile(String testDir, StringBuilder fileName, T objectToWrite) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File(Paths.get(testDir, fileName.toString()).toString()), objectToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static  <T> List<T> getObjectsFromDir(String testResultsDir, Class<T> objectType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Stream<Path> files = Files.list(Paths.get(testResultsDir));
        List<T> resultList = new ArrayList<>();
        files.forEach(filePath -> {

            try {
                T result = mapper.readValue(new File(filePath.toString()), objectType);
                resultList.add(result);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("failed to deserialize file {}", filePath.toString());
            }

        });

        return resultList;
    }

    public static void writeLine(FileWriter writer, String str) throws IOException {

        writer.write(str + Constants.NEW_LINE);
    }

}