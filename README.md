# cloud-network-test

#Sampler server:

build the artifact:
mvn package

google cloud services: <br />
1. from gcs console create ubuntu 16.04 vm, install iperf3 and java8 (http://tecadmin.net/install-oracle-java-8-jdk-8-ubuntu-via-ppa/)<br />
2. upload the artifact jar and scripts in scripts folder to the vm.<br />
3. copy start/stop_demo_service.sh to /usr/bin/local<br />
4. enable execution permissions "chmod +x {script}"<br />
5. copy demoservice.sh to /etc/init.d<br />
6. enable execution permissions<br />
7. update-rc.d demoservice.sh defaults<br />
8. reboot<br />
9. verify service is up "sudo lsof -i :8080"<br />
10. capture snapshot<br />
11. in gcs console open ports tcp: 8080, 5222-6222, udp: 5222-6222 and allow ICMP<br />

  <br />
aws:<br />
1. from aws console create ubuntu 16.04 vm, install iperf3 and java8 (http://tecadmin.net/install-oracle-java-8-jdk-8-ubuntu-via-ppa/)<br />
2. upload the artifact jar and scripts in scripts folder to the vm.<br />
3. copy start/stop_demo_service.sh to /usr/bin/local<br />
4. enable execution permissions "chmod +x {script}"<br />
5. copy demoservice.sh to /etc/init.d<br />
6. enable execution permissions<br />
7. update-rc.d demoservice.sh defaults<br />
8. reboot<br />
9. verify service is up "sudo lsof -i :8080"<br />
10. create image, ec2 -> right click instance -> image -> create image<br />

<br /><br />
#Manager Server:<br />

CloudProvider:<br />
0 - Google  <br />
1 - AWS<br />

1. set environment variables:<br />
  a. google.snapshot.name (the name of the snapshot from google bullet 10)<br />
  b. google.project.id (project id/name)<br />
  c. google.service.account.credentials.json (google service account json, can be created in the gcs console under API)<br />
  d. aws.base.image.ami (the image ami from aws bullet 10)<br />
  e. aws.vm.instance.type (desierd machine type e.g: t2.micro)<br />
  f. aws.base.image.name (the image name from aws bullet 10)<br />
  g. aws.base.image.region (the image region from aws bullet 10 e.g:us-west-2)<br />
  i. set cloud.aws.credentials.accessKey, cloud.aws.credentials.secretKey<br />
 
 run manager, and verify it's up:
 GET http://{manager-ip}:8080/manager/test
2. running bandwidth test:<br />
  Api: POST http://{manager-ip}:8080/manager/run/bandwidth<br />
  Json class: BandwidthTest.java<br />
  example:<br />
  ```json
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
  ```
  <br />
  you are responsible to set the number of servers on each vm. <br />
  
3. running latency test<br />
  Api: POST http://{manager-ip}:8080/manager/run/latency<br />
  Json class: LatencyTest.java<br />
  example:<br />
  ```json
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
     ```
