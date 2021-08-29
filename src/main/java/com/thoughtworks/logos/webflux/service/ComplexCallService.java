package com.thoughtworks.logos.webflux.service;

import com.thoughtworks.logos.webflux.component.WebFluxReactiveFeignClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ComplexCallService {

    private final WebFluxReactiveFeignClient webFluxReactiveFeignClient;

    public ComplexCallService(WebFluxReactiveFeignClient webFluxReactiveFeignClient) {
        this.webFluxReactiveFeignClient = webFluxReactiveFeignClient;
    }


    public Mono<String> complexCallByReactive(long delayOne, long delayTwo) {

        Mono<String> messageForOne = webFluxReactiveFeignClient.getMessage(delayOne);
        Mono<String> messageForTwo = webFluxReactiveFeignClient.getMessage(delayTwo);

        return Mono.zip(messageForOne, messageForTwo, (one, two) -> one + "," + two);
    }
}
