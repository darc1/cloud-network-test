package com.clean.network.measurment.orchestrator.cloud.aws;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by root on 1/16/17.
 */
@Configuration
@EnableContextCredentials
@EnableAutoConfiguration(exclude = AWSConfig.class)
public class AWSConfig {

    @Autowired
    AWSCredentialsProvider credentialsProvider;



    @Bean
    public AmazonEC2 getAmazonEC(){
        return new AmazonEC2Client(credentialsProvider);
    }
}
