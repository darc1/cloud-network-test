package com.vgn.cloud.bandwidth.cloud;

import com.vgn.cloud.bandwidth.domain.VMInstance;
import com.vgn.cloud.bandwidth.domain.VMTestInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by root on 1/12/17.
 */
public interface ICloudServices {

     List<VMInstance> createVMs(List<VMTestInfo> vmTestInfos) throws IOException, InterruptedException;
}
