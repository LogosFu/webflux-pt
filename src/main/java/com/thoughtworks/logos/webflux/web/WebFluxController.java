package com.thoughtworks.logos.webflux.web;

import com.thoughtworks.logos.webflux.component.NormalFeignClient;
import com.thoughtworks.logos.webflux.component.WebFluxReactiveFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class WebFluxController {

    private final WebFluxReactiveFeignClient webFluxReactiveFeignClient;
    private final NormalFeignClient normalFeignClient;

    public WebFluxController(WebFluxReactiveFeignClient webFluxReactiveFeignClient, NormalFeignClient normalFeignClient) {
        this.webFluxReactiveFeignClient = webFluxReactiveFeignClient;
        this.normalFeignClient = normalFeignClient;
    }

    @GetMapping("/async/delay/{param}")
    public Mono<String> async(@PathVariable long param) {
        return Mono.just("this is async web return")
                .delayElement(Duration.ofSeconds(param));
    }

    @GetMapping("/feign/{param}")
    public Mono<String> callReactiveFeignClient(@PathVariable long param) {
        return webFluxReactiveFeignClient.getMessage(param);
    }

    @GetMapping("/normal-feign/{param}")
    public String callNormalFeignClient(@PathVariable long param) {
        return normalFeignClient.getMessage(param);
    }

}
