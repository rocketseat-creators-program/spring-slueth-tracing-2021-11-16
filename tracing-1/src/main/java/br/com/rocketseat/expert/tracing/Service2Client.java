package br.com.rocketseat.expert.tracing;
import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.cloud.sleuth.BaggageInScope;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Service2Client {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final WebClient webClient;
	private final String serviceAddress;
	private final Tracer tracer;
	
	Service2Client(WebClient webClient, 
				   @Value("${service2.address:localhost:8082}") String serviceAddress, 
				   Tracer tracer){
		this.webClient = webClient;
		this.serviceAddress = serviceAddress; 
		this.tracer = tracer;
	}
	
	
	public  Mono<String> start(){
		log.info("Hello from service1 to service2.");
		Span mySpan = tracer.currentSpan();
		
		mySpan.tag("my-test-expert", "expert-club");
		
		Span newSpan = tracer.nextSpan();
		newSpan.tag("new-test-expert", "new-expert-club");
		
		
		return webClient.get()
				.uri("http://"+serviceAddress+"/service2")
				.exchange()
				.doOnSuccess(clientResponse -> {
					log.info("Got response from service2 [{}]", clientResponse);
                    try (BaggageInScope bs = this.tracer.getBaggage("key")) {
                        log.info("Service1: Baggage for [key] is [" + (bs == null ? null : bs.get()) + "]");
                    }
				})
				.flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
				.doOnTerminate(() -> {
                    
                });
				
	}
	
    @NewSpan("first_span")
    Mono<String> timeout(@SpanTag("someTag") String tag) throws InterruptedException {
        Thread.sleep(300);
        log.info("Hello from service1. Calling service2 - should end up with read timeout");
        return webClient.get()
                .uri("http://" + serviceAddress + "/readtimeout")
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, ClientResponse::createException)
                .bodyToMono(String.class);
    }
}

