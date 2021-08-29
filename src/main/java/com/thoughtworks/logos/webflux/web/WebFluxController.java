package com.thoughtworks.logos.webflux.web;

import com.thoughtworks.logos.webflux.component.NormalFeignClient;
import com.thoughtworks.logos.webflux.component.WebFluxReactiveFeignClient;
import com.thoughtworks.logos.webflux.service.ComplexCallService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class WebFluxController {

    private final WebFluxReactiveFeignClient webFluxReactiveFeignClient;
    private final NormalFeignClient normalFeignClient;
    private final ComplexCallService complexCallService;

    public WebFluxController(WebFluxReactiveFeignClient webFluxReactiveFeignClient, NormalFeignClient normalFeignClient, ComplexCallService complexCallService) {
        this.webFluxReactiveFeignClient = webFluxReactiveFeignClient;
        this.normalFeignClient = normalFeignClient;
        this.complexCallService = complexCallService;
    }

    @GetMapping("/async/delay/{param}")
    public Mono<String> async(@PathVariable long param) {
        return Mono.just("this is async web return")
                .delayElement(Duration.ofSeconds(param));
    }

    @GetMapping("/reactive/{param}")
    public Mono<String> callReactiveFeignClient(@PathVariable long param) {
        return webFluxReactiveFeignClient.getMessage(param);
    }

    @GetMapping("/normal/{param}")
    public String callNormalFeignClient(@PathVariable long param) {
        return normalFeignClient.getMessage(param);
    }

    @GetMapping("/reactive/{delay1}/{delay2}")
    public Mono<String> complexCallReactive(@PathVariable long delay1, @PathVariable long delay2) {
        return complexCallService.complexCallByReactive(delay1, delay2);
    }

    @GetMapping("/normal/{delay1}/{delay2}")
    public String complexCallNormal(@PathVariable long delay1, @PathVariable long delay2) {
        return complexCallService.complexCallByNormal(delay1, delay2);
    }

}
