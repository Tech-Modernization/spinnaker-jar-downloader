# spinnaker-jar-downloader
Script to test spinnaker JAR downloading

## Requirents
Install Java if you need it:

http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

Install Groovy:

`brew install groovy` Or
http://groovy-lang.org/download.html#distro

## Usage
`artifact` and `repository` are required
```
./test.groovy -[hraups]
  -a,--artifact         Artifact name
  -h,--help             Show usage information
  -p,--password         Password for basic auth
  -r,--repository       URL to JAR repository
  -u,--user             Username for basic auth
```

## Example
`./test.groovy -r http://nexus-2040588938.ca-central-1.elb.amazonaws.com/repository/hgallotest/ -a spring-cloud-spinnaker-1.0.0.BUILD-SNAPSHOT.jar`
