package com.vgn.cloud.bandwidth.cloud.aws;

import com.amazonaws.AmazonServiceException;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;


import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.vgn.cloud.bandwidth.cloud.ICloudServices;
import com.vgn.cloud.bandwidth.common.SafeFileSystem;
import com.vgn.cloud.bandwidth.domain.VMInstance;
import com.vgn.cloud.bandwidth.domain.VMTestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by root on 1/15/17.
 */
@Service
public class AmazonWebServices implements ICloudServices {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${aws.base.image.name}")
    private String imageName;

    @Value("${aws.base.image.ami}")
    private String baseSourceImageId;

    @Value("${aws.base.image.region}")
    private String sourceBaseImageRegion;

    @Value("${aws.vm.instance.type}")
    private String instanceType;

    private static final String KEY_PAIRS_DIR = "aws_key_pairs";
    private final int INSTANCE_STATE_RUNNING = 16;

    @Autowired
    private AmazonEC2 amazonEC2;


    @PostConstruct
    private void init(){
        Path dirPath = Paths.get(KEY_PAIRS_DIR);
        createDirIfNotExists(dirPath);
    }

    private void createDirIfNotExists(Path dirPath) {
        if(!SafeFileSystem.dirExists(dirPath)) {
            SafeFileSystem.createDir(dirPath);
        }
    }


    @Override
    public List<VMInstance> createVMs(List<VMTestInfo> vmTestInfos) throws IOException, InterruptedException {

        List<VMInstance> created = new ArrayList<>();

        for(VMTestInfo vm : vmTestInfos){

            amazonEC2.setRegion(Region.getRegion(Regions.fromName(vm.getRegion())));
            if(!vm.isCreateNew()){

                VMInstance createdInstance = getVMInstanceParams(vm);
                created.add(createdInstance);
                continue;
            }
            createSecurityGroup(vm);
            createKeyPair(vm);
            String imageAmiId = copyBaseImageToRegion();
            created.add(createVMInstance(vm, imageAmiId));
        }

        return created;

    }

    private VMInstance getVMInstanceParams(VMTestInfo vm) {

        Instance instance = getInstance(vm);

        VMInstance createdInstance = createVMInstance(vm, instance);

        return createdInstance;
    }

    private VMInstance createVMInstance(VMTestInfo vm, Instance instance) {
        VMInstance createdInstance = new VMInstance();
        createdInstance.setName(vm.getName());
        createdInstance.setZone(vm.getRegion());
        createdInstance.setPublicIp(instance.getPublicIpAddress());
        createdInstance.setPrivateIp(instance.getPrivateIpAddress());
        createdInstance.setTestInfo(vm);
        return createdInstance;
    }

    private Instance getInstance(VMTestInfo vm) {

        Filter nameFilter = new Filter().withName("tag:Name").withValues(vm.getName());

        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        describeInstancesRequest.withFilters(nameFilter);
        DescribeInstancesResult describeInstancesResult = amazonEC2.describeInstances(describeInstancesRequest);
        return describeInstancesResult.getReservations().get(0).getInstances().get(0);
    }

    private VMInstance createVMInstance(VMTestInfo vm, String ami) {

        RunInstancesRequest runInstancesRequest =
                new RunInstancesRequest();

        runInstancesRequest.withImageId(ami)
                .withInstanceType(instanceType)
                .withMinCount(1)
                .withMaxCount(1)
                .withKeyName(vm.getName())
                .withSecurityGroups(vm.getName());

        RunInstancesResult runInstancesResult = amazonEC2.runInstances(runInstancesRequest);
        String instanceId = runInstancesResult.getReservation().getInstances().get(0).getInstanceId();

        while (true){

            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest().withInstanceIds(instanceId);
            DescribeInstancesResult describeInstancesResult = amazonEC2.describeInstances(describeInstancesRequest);

            if(describeInstancesResult != null){

                Instance instance = describeInstancesResult.getReservations().get(0).getInstances().get(0);
                if(instance.getInstanceId().equals(instanceId)
                        && instance.getState().getCode() == INSTANCE_STATE_RUNNING){

                    setVMName(vm, instanceId);
                    VMInstance createdInstance = createVMInstance(vm, instance);
                    return createdInstance;
                }
            }
        }
    }

    private void setVMName(VMTestInfo vm, String instanceId) {
        Tag nameTag = new Tag().withKey("Name").withValue(vm.getName());
        CreateTagsRequest createTagsRequest = new CreateTagsRequest()
                .withResources(instanceId)
                .withTags(nameTag);
        amazonEC2.createTags(createTagsRequest);
    }

    private String copyBaseImageToRegion() throws InterruptedException {

        copyBaseImageIfNeeded();

        do {

            DescribeImagesResult result = getDescribeImagesResultByName(imageName);
            if(result.getImages() == null){
                Thread.sleep(2000);
                continue;
            }

            List<String> images = result.getImages().stream().map(x -> x.getName()).collect(Collectors.toList());
            if(!images.contains(imageName)){
                Thread.sleep(2000);
                continue;
            }

            for(Image image : result.getImages()){

                if(!image.getName().equals(imageName)) continue;
                if(image.getState().equals("available")){
                    return image.getImageId();
                }
            }

            Thread.sleep(2000);
            continue;

        }while (true);

    }

    private void copyBaseImageIfNeeded() {

        DescribeImagesResult describeImagesResult = getDescribeImagesResultByName(imageName);

        if(describeImagesResult.getImages() != null
                && !imageInResult(describeImagesResult)){

            CopyImageRequest request = new CopyImageRequest()
                    .withSourceRegion(sourceBaseImageRegion)
                    .withSourceImageId(baseSourceImageId)
                    .withName(imageName);

            CopyImageResult result = amazonEC2.copyImage(request);
        }
    }

    private DescribeImagesResult getDescribeImagesResultByName(String imageName) {
        Filter name = new Filter().withName("name").withValues(imageName);
        DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest().withFilters(name);
        return amazonEC2.describeImages(describeImagesRequest);
    }

    private boolean imageInResult(DescribeImagesResult describeImagesRequest) {
        return describeImagesRequest.getImages().stream().map(x -> x.getName())
        .collect(Collectors.toList()).contains(imageName);
    }

    private void createKeyPair(VMTestInfo vm) {

        DescribeKeyPairsResult describeKeyPairsResult = amazonEC2.describeKeyPairs(new DescribeKeyPairsRequest());
        List<KeyPairInfo> keyPairs = describeKeyPairsResult.getKeyPairs();
        if(keyPairs == null || !keyPairs.stream().map(x -> x.getKeyName()).collect(Collectors.toList()).contains(vm.getName()))
        {
            CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest().withKeyName(vm.getName());
            CreateKeyPairResult createKeyPairResult = amazonEC2.createKeyPair(createKeyPairRequest);

            writeKeyPairsToFile(vm, createKeyPairResult);
        }
    }

    private void writeKeyPairsToFile(VMTestInfo vm, CreateKeyPairResult createKeyPairResult) {
        Path dirName = Paths.get(KEY_PAIRS_DIR, vm.getRegion());
        createDirIfNotExists(dirName);

        SafeFileSystem.writeAllText(Paths.get(dirName.toString(), vm.getName()+"FingerPrint"),
                createKeyPairResult.getKeyPair().getKeyFingerprint());
        SafeFileSystem.writeAllText(Paths.get(dirName.toString(), vm.getName()+"Material"),
                createKeyPairResult.getKeyPair().getKeyMaterial());
        SafeFileSystem.writeAllText(Paths.get(dirName.toString(), vm.getName()+"KeyName"),
                createKeyPairResult.getKeyPair().getKeyName());
    }

    private void createSecurityGroup(VMTestInfo vm) {

        try {
            CreateSecurityGroupRequest securityGroupRequest = new CreateSecurityGroupRequest(
                    vm.getName(), vm.getName());
            CreateSecurityGroupResult result = amazonEC2
                    .createSecurityGroup(securityGroupRequest);
            System.out.println(String.format("Security group created: [%s]",
                    result.getGroupId()));
        } catch (AmazonServiceException ase) {
            // Likely this means that the group is already created, so ignore.
            System.out.println(ase.getMessage());
        }
        String ipAddress = "0.0.0.0/0";

        List<String> ipRanges = Collections.singletonList(ipAddress);
        List<IpPermission> permissions = createPermissions(ipRanges);


        try {
            // Authorize the ports to the used.
            AuthorizeSecurityGroupIngressRequest ingressRequest = new AuthorizeSecurityGroupIngressRequest(
                    vm.getName(), permissions);
            amazonEC2.authorizeSecurityGroupIngress(ingressRequest);
            logger.info(String.format("Ingress port authorized: [{}]",
                    permissions.toString()));
        } catch (AmazonServiceException ase) {
            // Ignore because this likely means the zone has already been authorized.
            System.out.println(ase.getMessage());
            logger.info(ase.getMessage());
        }
    }

    private List<IpPermission> createPermissions(List<String> ipRanges) {
        List<IpPermission> permissions = new ArrayList<>();

        permissions.add(new IpPermission()
                .withIpProtocol("icmp")
                .withFromPort(new Integer(-1))
                .withToPort(new Integer(-1))
                .withIpRanges(ipRanges));

        permissions.add(new IpPermission()
                .withIpProtocol("tcp")
                .withFromPort(new Integer(8080))
                .withToPort(new Integer(8080))
                .withIpRanges(ipRanges));


        permissions.add(new IpPermission()
                .withIpProtocol("tcp")
                .withFromPort(new Integer(5222))
                .withToPort(new Integer(6222))
                .withIpRanges(ipRanges));

        permissions.add(new IpPermission()
                .withIpProtocol("udp")
                .withFromPort(new Integer(5222))
                .withToPort(new Integer(6222))
                .withIpRanges(ipRanges));
        return permissions;
    }
}
