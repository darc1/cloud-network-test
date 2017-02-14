package com.clean.network.measurment.orchestrator.cloud;


import domain.VMInstance;
import domain.VMTestInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by root on 1/12/17.
 */
public interface ICloudServices {

     List<VMInstance> createVMs(List<VMTestInfo> vmTestInfos) throws IOException, InterruptedException;
}
