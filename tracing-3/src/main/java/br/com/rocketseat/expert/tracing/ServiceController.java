package br.com.rocketseat.expert.tracing;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ServiceController {
	
	@RequestMapping("/bar")
	public String serviceMethodInController() throws InterruptedException {
        Thread.sleep(300);
        log.info("Hello from service3");
        return "Hello from service3";
    }
	
}
