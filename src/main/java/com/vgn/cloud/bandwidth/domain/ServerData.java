package com.vgn.cloud.bandwidth.domain;

/**
 * Created by cdoar on 1/9/17.
 */
public class ServerData {

    private VMInstance vmInstance;
    private int listeningPort;
    private boolean isAssigned;

    public ServerData() {
    }

    public ServerData(VMInstance vmInstance, int listeningPort) {
        this.vmInstance = vmInstance;
        this.listeningPort = listeningPort;
        isAssigned = false;

    }

    public int getListeningPort() {
        return listeningPort;
    }

    public VMInstance getVmInstance() {
        return vmInstance;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }
}
