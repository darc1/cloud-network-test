package com.vgn.cloud.bandwidth.cloud;

import com.vgn.cloud.bandwidth.cloud.aws.AmazonWebServices;
import com.vgn.cloud.bandwidth.cloud.google.GoogleCloudServices;
import com.vgn.cloud.bandwidth.domain.VMInstance;
import com.vgn.cloud.bandwidth.domain.VMTestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by root on 1/12/17.
 */

@Service
public class UnifiedCloudInstanceCreator {

    @Autowired
    private GoogleCloudServices gcs;

    @Autowired
    private AmazonWebServices aws;

    private Map<CloudProvider, ICloudServices> cloudServices = new HashMap<>();

    public UnifiedCloudInstanceCreator(){


    }

    @PostConstruct
    private void init() {
        cloudServices.put(CloudProvider.GCS, gcs);
        cloudServices.put(CloudProvider.AWS, aws);
    }


    public List<VMInstance> getOrCreateVmInstances(Collection<VMTestInfo> vmTestInfos) throws IOException, InterruptedException {

        Map<CloudProvider, List<VMTestInfo>> vmsByCloud = getVMsByCloud(vmTestInfos);
        List<VMInstance> result = new ArrayList<>();
        for (CloudProvider cloudProvider : CloudProvider.values()) {

            if(vmsByCloud.containsKey(cloudProvider) && cloudServices.containsKey(cloudProvider)){

                ICloudServices cloudServices = this.cloudServices.get(cloudProvider);
                List<VMTestInfo> vmInfos = vmsByCloud.get(cloudProvider);
                result.addAll(cloudServices.createVMs(vmInfos));
            }

        }

        return result;
    }

    private Map<CloudProvider, List<VMTestInfo>> getVMsByCloud(Collection<VMTestInfo> vmTestInfos) {

        Map<CloudProvider, List<VMTestInfo>> result = new HashMap<>();
        for(VMTestInfo vmTestInfo : vmTestInfos){

            if(!result.containsKey(vmTestInfo.getCloudProvider())){
                result.put(vmTestInfo.getCloudProvider(), new ArrayList<>());
            }

            result.get(vmTestInfo.getCloudProvider()).add(vmTestInfo);
        }

        return result;

    }

}
