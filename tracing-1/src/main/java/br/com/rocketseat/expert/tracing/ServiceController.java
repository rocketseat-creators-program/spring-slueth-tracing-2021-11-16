package br.com.rocketseat.expert.tracing;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class ServiceController {

    private final Service2Client service2Client;

    public ServiceController(Service2Client service2Client) {
        this.service2Client = service2Client;
    }

    @GetMapping("/start/{name}")
    public Mono<String> start(@PathVariable("name") String name) {
        log.info("Hello example "+ name);     
    	return this.service2Client.start();
    }

    @GetMapping("/readtimeout")
    public Mono<String> timeout() throws InterruptedException {
        return service2Client.timeout(LocalDateTime.now().toString());
    }

    @PostMapping("/start")
    public Mono<String> postStart() {
        return start("TESTE");
    }

    @PostMapping("/readtimeout")
    public Mono<String> postTimeout() throws InterruptedException {
        return timeout();
    }
}
