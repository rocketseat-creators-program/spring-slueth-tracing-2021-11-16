package br.com.rocketseat.expert.tracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.BaggageInScope;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ServiceController {
	
	private static final Logger log = LoggerFactory.getLogger(ServiceController.class);

    private final RestTemplate restTemplate;
    private final Tracer tracer;
    private final String serviceAddress3;
    private final String serviceAddress4;
    private final int port;
    
    ServiceController(RestTemplate restTemplate, Tracer tracer,
            @Value("${service3.address:localhost:8083}") String serviceAddress3,
            @Value("${service4.address:localhost:8084}") String serviceAddress4,
            @Value("${server.port:8082}") int port) {
		this.restTemplate = restTemplate;
		this.tracer = tracer;
		this.serviceAddress3 = serviceAddress3;
		this.serviceAddress4 = serviceAddress4;
		this.port = port;
	}
    
    @RequestMapping("/service2")
    public String service2MethodInController() throws InterruptedException {
    	Thread.sleep(200);
        try (BaggageInScope baggage = this.tracer.getBaggage("key")) {
            log.info("Service2: Baggage for [key] is [" + (baggage == null ? null : baggage.get()) + "]");
            log.info("Hello from service2. Calling service3 and then service4");
            String service3 = restTemplate.getForObject("http://" + serviceAddress3 + "/bar", String.class);
            log.info("Got response from service3 [{}]", service3);
            String service4 = restTemplate.getForObject("http://" + serviceAddress4 + "/baz", String.class);
            log.info("Got response from service4 [{}]", service4);
            return String.format("Hello from service2, response from service3 [%s] and from service4 [%s]", service3, service4);
        }
    }
	
}
