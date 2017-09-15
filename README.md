# spinnaker-jar-downloader

## Install Java
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

## Install Groovy
`brew install groovy`
Or
http://groovy-lang.org/download.html#distro

## Usage
```
usage: ./downloadjar.groovy -[hraups]
  -r,--repository       URL to JAR repository
  -a,--artifact         Artifact name
  -h,--help             Show usage information
  -u,--user             Username for basic auth
  -p,--password         Password for basic auth
  -s,--serverGroupName  serverGroupName
```

## Example
`./downloadjar.groovy -r http://jar.repo.com -u user -a artifact.jar -p password`
