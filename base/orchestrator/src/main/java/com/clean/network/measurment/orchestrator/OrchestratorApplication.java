package com.clean.network.measurment.orchestrator;

import cloud.CloudProvider;
import com.clean.network.measurment.orchestrator.cloud.aws.AWSRegions;
import com.clean.network.measurment.orchestrator.cloud.google.GoogleRegions;
import com.clean.network.measurment.orchestrator.service.tests.LongRunIperf3Test;
import domain.BandwidthTest;
import domain.LongRunBandwidthTest;
import domain.VMTestInfo;
import domain.VMTestTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class OrchestratorApplication {

	@Autowired
	static LongRunIperf3Test test;

	public static void main(String[] args) {

		SpringApplication.run(OrchestratorApplication.class, args);

	}


}
