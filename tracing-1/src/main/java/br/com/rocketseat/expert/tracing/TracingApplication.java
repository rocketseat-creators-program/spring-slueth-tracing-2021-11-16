package br.com.rocketseat.expert.tracing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class TracingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TracingApplication.class, args);
	}
	
	@Bean
    WebClient webClient() {
        return WebClient.create();
    }
}
