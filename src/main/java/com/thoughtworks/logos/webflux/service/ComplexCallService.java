package com.thoughtworks.logos.webflux.service;

import com.thoughtworks.logos.webflux.component.NormalFeignClient;
import com.thoughtworks.logos.webflux.component.WebFluxReactiveFeignClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ComplexCallService {

    private final WebFluxReactiveFeignClient webFluxReactiveFeignClient;
    private final NormalFeignClient normalFeignClient;
    private final BlockedThreadService blockedThreadService;

    public ComplexCallService(WebFluxReactiveFeignClient webFluxReactiveFeignClient, NormalFeignClient normalFeignClient, BlockedThreadService blockedThreadService) {
        this.webFluxReactiveFeignClient = webFluxReactiveFeignClient;
        this.normalFeignClient = normalFeignClient;
        this.blockedThreadService = blockedThreadService;
    }


    public Mono<String> complexCallByReactive(long delayOne, long delayTwo) {

        Mono<String> messageForOne = webFluxReactiveFeignClient.getMessage(delayOne).log();
        Mono<String> messageForTwo = webFluxReactiveFeignClient.getMessage(delayTwo).log();

        return Mono.zip(messageForOne, messageForTwo, (one, two) -> one + "," + two);
    }

    public Mono<String> complexCallByReactiveBlock(long delayOne, long delayTwo) {

        Mono<String> messageForOne = webFluxReactiveFeignClient.getMessage(delayOne);
        Mono<String> messageForTwo = webFluxReactiveFeignClient.getMessage(delayTwo);
        String message = messageForOne.block(Duration.ofMinutes(3));
        String message2 = messageForTwo.block(Duration.ofMinutes(3));
        return Mono.just(message + "," + message2);
    }

    public String complexCallByNormal(long delayOne, long delayTwo) {
        String messageForOne = normalFeignClient.getMessage(delayOne);
        String messageForTwo = normalFeignClient.getMessage(delayTwo);

        return messageForOne + "," + messageForTwo;
    }

    public String complexCallByNormalAsync(long delayOne, long delayTwo) throws ExecutionException, InterruptedException {
//        Future<String> callBackendFuture1 =
        CompletableFuture<String> delay1 = CompletableFuture.supplyAsync(() -> normalFeignClient.getMessage(delayOne));
        CompletableFuture<String> delay2 = CompletableFuture.supplyAsync(() -> normalFeignClient.getMessage(delayTwo));
        CompletableFuture<Void> all = CompletableFuture.allOf(delay1, delay2);
        all.get();
        return delay1.get() + "," + delay2.get();
    }

    public Mono<Void> startLogThread() {
        blockedThreadService.blockedThread();
        return Mono.empty();
    }
}
