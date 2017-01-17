# cloud-network-test

#Sampler server:

build the artifact:
mvn package

google cloud services: 
1. from gcs console create ubuntu 16.04 vm, install iperf3 and java8 (http://tecadmin.net/install-oracle-java-8-jdk-8-ubuntu-via-ppa/)
2. upload the artifact jar and scripts in scripts folder to the vm.
3. copy start/stop_demo_service.sh to /usr/bin/local
4. enable execution permissions "chmod +x {script}"
5. copy demoservice.sh to /etc/init.d
6. enable execution permissions
7. update-rc.d demoservice.sh defaults
8. reboot
9. verify service is up "sudo lsof -i :8080"
10. capture snapshot
11. in gcs console open ports tcp: 8080, 5222-6222, udp: 5222-6222 and allow ICMP

  
aws:
1. from aws console create ubuntu 16.04 vm, install iperf3 and java8 (http://tecadmin.net/install-oracle-java-8-jdk-8-ubuntu-via-ppa/)
2. upload the artifact jar and scripts in scripts folder to the vm.
3. copy start/stop_demo_service.sh to /usr/bin/local
4. enable execution permissions "chmod +x {script}"
5. copy demoservice.sh to /etc/init.d
6. enable execution permissions
7. update-rc.d demoservice.sh defaults
8. reboot
9. verify service is up "sudo lsof -i :8080"
10. create image, ec2 -> right click instance -> image -> create image


#Manager Server:

CloudProvider:
0 - Google
1 - AWS

1. set environment variables:
  a. google.snapshot.name (the name of the snapshot from google bullet 10)
  b. google.project.id (project id/name)
  c. google.service.account.credentials.json (google service account json, can be created in the gcs console under API)
  d. aws.base.image.ami (the image ami from aws bullet 10)
  e. aws.vm.instance.type (desierd machine type e.g: t2.micro)
  f. aws.base.image.name (the image name from aws bullet 10)
  g. aws.base.image.region (the image region from aws bullet 10 e.g:us-west-2)
  i. set cloud.aws.credentials.accessKey, cloud.aws.credentials.secretKey
  
2. running bandwidth test:
  Api: POST http://{manager-ip}:8080/manager/run/bandwidth
  Json class: BandwidthTest.java
  example:
  {
	"testId": "bandwidth_aws_fran_seoul",
	"testDurationInSec": 120,
	"testVMs": [{
			"name": "fran-1",
			"region": "eu-central-1",
			"numberOfServers": 0,
			"cloudProvider": 1,
			"createNew": false,
			"targets": [{
					"name": "seoul-1",
					"region": "ap-northeast-2",
					"connections": 1}]
		}, {
			"name": "seoul-1",
			"region": "ap-northeast-2",
			"numberOfServers": 1,
			"cloudProvider": 1,
			"createNew": false,
			"targets": []
		  }
	  ]
  }
  you are responsible to set the number of servers on each vm. 
  
3. running latency test
  Api: POST http://{manager-ip}:8080/manager/run/latency
  Json class: LatencyTest.java
  example:
  {
	"testId": "latency",
	"testParams":{
		"numberOfRuns":10,
		"waitIntervalBetweenSamples":200,
		"destinationIp":""
	},
	"testVMs": [{
			"name": "sushi-2a",
			"region": "asia-northeast1-a",
			"numberOfServers": 0,
			"cloudProvider": 0,
			"createNew": false,
			"targets": [{
					"name": "waffle-2d",
					"region": "europe-west1-d",
					"connections": 1
				}
			]

		},{	"name": "waffle-2d",
			"region": "europe-west1-d",
			"numberOfServers": 1,
			"cloudProvider": 0,
			"createNew": false,
			"targets": []
      }]
     }
