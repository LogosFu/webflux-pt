package com.thoughtworks.logos.webflux;

import org.springframework.web.bind.annotation.GetMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "webFluxReactiveFeignClient", url = "localhost:18080")
public interface WebFluxReactiveFeignClient {
    @GetMapping("/async/delay/1")
    Mono<String> getMessage();
}
