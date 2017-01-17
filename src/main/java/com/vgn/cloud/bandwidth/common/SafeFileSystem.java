package com.vgn.cloud.bandwidth.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by root on 1/15/17.
 */
public class SafeFileSystem {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);


    public static boolean dirExists(Path dirPath){
        return Files.exists(dirPath);
    }

    public static boolean createDir(Path dirPath) {
        try {
            Files.createDirectory(dirPath);
            return true;
        } catch (IOException e) {
            logger.error("failed to create dir: " + dirPath,e);
            return false;
        }
    }

    public static boolean writeAllText(Path filePath, String text){

        try {
            Files.write(filePath, text.getBytes());
            return true;
        } catch (IOException e) {
            logger.error("failed to write to file: " + filePath, e);
            return false;
        }
    }

}
