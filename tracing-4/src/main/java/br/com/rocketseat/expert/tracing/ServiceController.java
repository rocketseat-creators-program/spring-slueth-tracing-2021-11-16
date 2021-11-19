package br.com.rocketseat.expert.tracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ServiceController {
    private static final Logger log = LoggerFactory.getLogger(ServiceController.class);

    @RequestMapping("/baz")
    public String serviceMethodInController() throws InterruptedException {
        Thread.sleep(300);
        log.info("Hello from service4");
        return "Hello from service4";
    }
}