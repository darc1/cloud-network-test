package com.vgn.cloud.bandwidth.domain;

import com.vgn.cloud.bandwidth.cloud.CloudProvider;

import java.util.List;

/**
 * Created by cdoar on 1/9/17.
 */
public class VMTestInfo {

    private String name;
    private String region;
    private int numberOfServers;
    private boolean createNew;
    private CloudProvider cloudProvider;
    private List<VMTestTarget> targets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getNumberOfServers() {
        return numberOfServers;
    }

    public void setNumberOfServers(int numberOfServers) {
        this.numberOfServers = numberOfServers;
    }

    public boolean isCreateNew() {
        return createNew;
    }

    public void setCreateNew(boolean createNew) {
        this.createNew = createNew;
    }

    public CloudProvider getCloudProvider() {
        return cloudProvider;
    }

    public void setCloudProvider(CloudProvider cloudProvider) {
        this.cloudProvider = cloudProvider;
    }

    public List<VMTestTarget> getTargets() {
        return targets;
    }

    public void setTargets(List<VMTestTarget> targets) {
        this.targets = targets;
    }
}
