
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
	  requestHeaders.set(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encodeBase64String("${description.username}:${description.password}".getBytes()))
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
	  responseBytes = restTemplate.exchange("${basePath}${description.artifact}".toString(), HttpMethod.GET, requestEntity, byte[])
	  contentLength = responseBytes != null ? responseBytes.getBody().length : 0;
	}

	println "Successfully downloaded ${contentLength} bytes"

	
	//File file = File.createTempFile("download.jar", null)
	//FileOutputStream fout = new FileOutputStream(file)
	//fout.write(responseBytes.body)
	//fout.close()

  }



def description = new CloudFoundryDeployDescription ("http://repo.spring.io/libs-snapshot/org/springframework/cloud/spring-cloud-spinnaker/1.0.0.BUILD-SNAPSHOT","spring-cloud-spinnaker-1.0.0.BUILD-SNAPSHOT.jar","A","B")
downloadJarFileFromWeb(description)


