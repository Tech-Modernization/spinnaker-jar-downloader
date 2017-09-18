#!/usr/bin/env groovy -cp ./commons-cli-1.3.1.jar:./clouddriver-cf-0.1.0-SNAPSHOT.jar:./commons-codec-1.10.jar:./commons-logging-1.2.jar:./spring-core-4.3.6.RELEASE.jar:./spring-web-4.3.6.RELEASE.jar

import org.springframework.http.*
import org.springframework.util.*
import com.netflix.spinnaker.clouddriver.cf.utils.RestTemplateFactory
import org.apache.commons.codec.binary.Base64

import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

class DefaultRestTemplateFactory implements RestTemplateFactory {

  @Override
  RestTemplate createRestTemplate() {
	new RestTemplate(requestFactory: new SimpleClientHttpRequestFactory(bufferRequestBody: false))
  }

}


class CloudFoundryDeployDescription {
	String repository
	String artifact
	String username
	String password
	CloudFoundryDeployDescription (repo,artif,name,pass) {
		this.repository = repo
		this.artifact = artif
		this.username = name
		this.password = pass
	}
}

def downloadJarFileFromWeb(CloudFoundryDeployDescription description) {
	HttpHeaders requestHeaders = new HttpHeaders()

	if (description.username && description.password) {
    encodedCreds = Base64.encodeBase64String("${description.username}:${description.password}".getBytes())
    println "Using Base64 encoded credentials: ${encodedCreds}"
    requestHeaders.set(HttpHeaders.AUTHORIZATION, "Basic " + encodedCreds)
	}

	def requestEntity = new HttpEntity<>(requestHeaders)
	defaultRestTemplateFactory = new DefaultRestTemplateFactory ()

	def restTemplate = defaultRestTemplateFactory.createRestTemplate()

	long contentLength = -1
	ResponseEntity<byte[]> responseBytes

	while (contentLength == -1 || contentLength != responseBytes?.headers?.getContentLength()) {
	  if (contentLength > -1) {
		task.updateStatus BASE_PHASE, "Downloaded ${contentLength} bytes, but ${responseBytes.headers.getContentLength()} expected! Retry..."
	  }
	  def basePath = description.repository + (description.repository.endsWith('/') ? '' : '/')
    println "Downloading ${description.artifact} from repository with User: ${description.username}"
	  responseBytes = restTemplate.exchange("${basePath}${description.artifact}".toString(), HttpMethod.GET, requestEntity, byte[])
	  contentLength = responseBytes != null ? responseBytes.getBody().length : 0;
	}

	println "Successfully downloaded ${contentLength} bytes for ${description.artifact}"


	//File file = File.createTempFile("download.jar", null)
	//FileOutputStream fout = new FileOutputStream(file)
	//fout.write(responseBytes.body)
	//fout.close()

}

def returnOpts(args) {
    def cli = new CliBuilder(usage: './downloadjar.groovy -[hraups]')
    // Create the list of options.
    cli.with {
        h longOpt: 'help', 'Show usage information'
        r longOpt: 'repository', required: true, args: 1, 'URL to JAR repository'
        a longOpt: 'artifact', required: true, args: 1, 'Artifact name'
        u longOpt: 'user', args: 1, 'Username for basic auth'
        p longOpt: 'password', args: 1, 'Password for basic auth'
    }

    def opts = cli.parse(args)

    // Show usage text when -h or --help option is used.
    if (opts.h) {
        cli.usage()
        // Will output:
        // usage: ./test.groovy -[hraups]
        //  -r,--repository       URL to JAR repository
        //  -a,--artifact         Artifact name
        //  -h,--help             Show usage information
        //  -u,--user             Username for basic auth
        //  -p,--password         Password for basic auth
        System.exit(0)
    }
    return opts
}

def doDownload(args) {
    def options = returnOpts(args)
    def description = new CloudFoundryDeployDescription (options."repository",options."artifact",options."user",options."password")
    downloadJarFileFromWeb(description)
}

doDownload(args)
