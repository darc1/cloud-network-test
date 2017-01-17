package com.vgn.cloud.bandwidth.domain.manager;

import com.vgn.cloud.bandwidth.domain.sampler.IcmpLatencySample;

/**
 * Created by root on 1/15/17.
 */
public class LatencyTestResult {

    private String source;
    private String target;
    private String sourceZone;
    private String targetZone;
    private IcmpLatencySample[] samples;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSourceZone() {
        return sourceZone;
    }

    public void setSourceZone(String sourceZone) {
        this.sourceZone = sourceZone;
    }

    public String getTargetZone() {
        return targetZone;
    }

    public void setTargetZone(String targetZone) {
        this.targetZone = targetZone;
    }

    public IcmpLatencySample[] getSamples() {
        return samples;
    }

    public void setSamples(IcmpLatencySample[] samples) {
        this.samples = samples;
    }
}
