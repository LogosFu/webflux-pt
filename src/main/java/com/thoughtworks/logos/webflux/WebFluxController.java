package com.thoughtworks.logos.webflux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class WebFluxController {

    @GetMapping("/async/delay/{param}")
    public Mono<String> async(@PathVariable long param) {
        return Mono.just("this is async web return")
                .delayElement(Duration.ofSeconds(param));
    }

    @GetMapping("/sync/delay/{param}")
    public Mono<String> hello(@PathVariable long param) {
        return Mono.just("this is sync web return")
                .delayElement(Duration.ofSeconds(param));
    }
}
