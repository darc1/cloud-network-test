package com.clean.network.measurment.orchestrator.cloud.google;


import com.clean.network.measurment.orchestrator.cloud.ICloudServices;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
import com.google.api.services.compute.model.*;
import domain.VMInstance;
import domain.VMTestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdoar on 1/8/17.
 */
@Service
public class GoogleCloudServices implements ICloudServices {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private Compute compute;

    @Value("${google.project.id}")
    private String projectId;

    @Value("${google.service.account.credentials.json:null}")
    private String serviceAccountCredentials;

    private static String client_email; //"709560193674-compute@developer.gserviceaccount.com";

    @Value("${google.snapshot.name}")
    private String snapshotName;

    private static final String NETWORK_ACCESS_CONFIG_TYPE = "ONE_TO_ONE_NAT";
    private static final String NETWORK_ACCESS_CONF_NAME = "External NAT";
    private static final String DISK_MODE_RW = "READ_WRITE";


    private GoogleCredential authenticate() throws IOException {

        InputStream stream = new ByteArrayInputStream(serviceAccountCredentials.getBytes(StandardCharsets.UTF_8));
        GoogleCredential credential = GoogleCredential.fromStream(stream).createScoped(ComputeScopes.all());
        client_email = credential.getServiceAccountId();
        return credential;
    }

    public GoogleCloudServices() throws IOException, GeneralSecurityException {

        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    }

    private Compute getCompute() throws IOException {

        if(compute == null){
            compute = new Compute.Builder(httpTransport, JSON_FACTORY, null)
                    .setHttpRequestInitializer(authenticate()).build();
        }

        return compute;
    }

    public List<VMInstance> createVMs(List<VMTestInfo> vms) throws IOException, InterruptedException {

        Compute.Snapshots.Get snapshots = getCompute().snapshots().get(projectId, snapshotName);
        Snapshot snapshot = snapshots.execute();

        List<VMInstance> result = new ArrayList<>();
        List<String> createdVMs = new ArrayList<>();
        for (VMTestInfo vmInfo : vms) {

            if(vmInfo.isCreateNew()){
                createVM(snapshot, vmInfo);
            }

            createdVMs.add(vmInfo.getName());
        }

        for(VMTestInfo vmInfo : vms){

            Instance instance = getInstance(vmInfo);
            if(instance == null){
                logger.error("failed to find instance {}, in region {}", vmInfo.getName(), vmInfo.getRegion());
            }

            String networkIP = getNetworkIP(instance);
            String natIP = getPublicIP(instance);

            while (natIP == null || networkIP == null){
                logger.warn("couldn't find network ips, refreshing data");
                Instance refreshedInstance = getInstance(vmInfo);
                natIP = getPublicIP(refreshedInstance);
                networkIP = getNetworkIP(refreshedInstance);
                Thread.sleep(5000);
            }

            VMInstance e = createVmInstance(vmInfo, instance, networkIP, natIP);
            result.add(e);
        }

        return result;
    }

    private VMInstance createVmInstance(VMTestInfo vmInfo, Instance instance, String networkIP, String natIP) {
        VMInstance e = new VMInstance();
        e.setName(instance.getName());
        e.setPrivateIp(networkIP);
        e.setPublicIp(natIP);
        e.setZone(vmInfo.getRegion());
        e.setTestInfo(vmInfo);
        return e;
    }

    private Instance getInstance(VMTestInfo vmInfo) throws IOException {
        return getCompute().instances()
                .get(projectId, vmInfo.getRegion(), vmInfo.getName())
                .execute();
    }

    private String getPublicIP(Instance instance) {
        return instance.getNetworkInterfaces().get(0)
                .getAccessConfigs().get(0).getNatIP();
    }

    private String getNetworkIP(Instance instance) {
        return instance.getNetworkInterfaces().get(0).getNetworkIP();
    }


    private String createVM(Snapshot snapshot, VMTestInfo vmInfo) throws IOException, InterruptedException {
        String deviceName = vmInfo.getName();

        List<AttachedDisk> attachedDisks = createDiskFromSnapshot(snapshot, vmInfo.getRegion(), deviceName);
        List<NetworkInterface> networkInterfaces = createNetworkInterface(vmInfo.getRegion());

        List<ServiceAccount> serviceAccounts = createServiceAccount();

        String machineTypeLink = getMachineType(vmInfo);

        Instance newInstance = new Instance()
                                .setDisks(attachedDisks)
                                .setName(deviceName)
                                .setNetworkInterfaces(networkInterfaces)
                                .setMachineType(machineTypeLink)
                                .setZone(vmInfo.getRegion())
                                .setServiceAccounts(serviceAccounts);
        Compute.Instances.Insert insert = getCompute().instances().insert(projectId, vmInfo.getRegion(), newInstance);

        insertVM(insert);
        return deviceName;
    }

    private void insertVM(Compute.Instances.Insert insert) throws IOException, InterruptedException {

        while (true){
            try {
                Operation res = insert.execute();
                break;

            }catch (GoogleJsonResponseException gjre){

                if(gjre.getDetails().getCode() == 400
                        && gjre.getDetails().getMessage().contains("is not ready")) {
                    Thread.sleep(5000L);
                    continue;
                }
                throw gjre;

            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }


    }

    private String getMachineType(VMTestInfo vmTestInfo) throws IOException {
        String machineType = "n1-standard-1";
        if(vmTestInfo.getMachineType() != null){
            machineType = vmTestInfo.getMachineType();
        }
        Compute.MachineTypes.Get machine = getCompute().machineTypes().get(projectId, vmTestInfo.getRegion(),
                machineType);
        MachineType machineGet = machine.execute();
        return machineGet.getSelfLink();
    }

    private List<ServiceAccount> createServiceAccount() {
        ServiceAccount account = new ServiceAccount()
                .setEmail(client_email)
                .setScopes(new ArrayList<>(ComputeScopes.all()));
        List<ServiceAccount> serviceAccounts = new ArrayList<>();
        serviceAccounts.add(account);
        return serviceAccounts;
    }

    private List<NetworkInterface> createNetworkInterface(String region) throws IOException {

        Compute.Networks.List networks = getCompute().networks().list(projectId);
        NetworkList foundNetworks = networks.execute();

        Compute.Subnetworks.List subnets = getCompute().subnetworks().list(projectId,
                getRegionFromZone(region));
        SubnetworkList subnetList = subnets.execute();

        List<AccessConfig> accessConfig = new ArrayList<>();

        accessConfig.add(new AccessConfig().setName(NETWORK_ACCESS_CONF_NAME).setType(NETWORK_ACCESS_CONFIG_TYPE));

        NetworkInterface networkInterface = new NetworkInterface()
                .setNetwork(foundNetworks.getItems().get(0).getSelfLink())
                .setSubnetwork(subnetList.getItems().get(0).getSelfLink())
                .setAccessConfigs(accessConfig);

        List<NetworkInterface> networkInterfaces = new ArrayList<>();
        networkInterfaces.add(networkInterface);
        return networkInterfaces;
    }

    private String getRegionFromZone(String region) {
        return region.substring(0, region.lastIndexOf('-'));
    }

    private List<AttachedDisk> createDiskFromSnapshot(Snapshot snapshot, String region, String deviceName) throws IOException {
        Disk disk1 = new Disk()
                .setSourceSnapshot(snapshot.getSelfLink())
                .setName(deviceName)
                .setSizeGb(10L)
                .setZone(region);

        Compute.Disks.Insert disks = getCompute().disks().insert(projectId, region, disk1);
        Operation disk = disks.execute();

        Compute.Disks.Get getDisk = getCompute().disks().get(projectId, region, deviceName);
        Disk found = getDisk.execute();

        AttachedDisk ad1 = new AttachedDisk()
                .setDeviceName(deviceName)
                .setBoot(true)
                .setMode(DISK_MODE_RW)
                .setAutoDelete(true)
                .setSource(found.getSelfLink());

        List<AttachedDisk> attachedDisks = new ArrayList<AttachedDisk>();
        attachedDisks.add(ad1);
        return attachedDisks;
    }

}
