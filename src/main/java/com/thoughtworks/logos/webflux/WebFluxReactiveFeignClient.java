package com.thoughtworks.logos.webflux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "webFluxReactiveFeignClient", url = "localhost:18080")
public interface WebFluxReactiveFeignClient {
    @GetMapping("/async/delay/{param}")
    Mono<String> getMessage(@PathVariable Long param);
}
