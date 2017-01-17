package com.vgn.cloud.bandwidth.service.csv;

import java.io.IOException;

/**
 * Created by cdoar on 1/10/17.
 */
public interface ICsvCreator {

    String createFromTestData(String testResultsDir) throws IOException;

}
