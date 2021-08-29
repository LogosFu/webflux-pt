package com.thoughtworks.logos.webflux.service;

import com.thoughtworks.logos.webflux.component.NormalFeignClient;
import com.thoughtworks.logos.webflux.component.WebFluxReactiveFeignClient;
import io.netty.util.concurrent.Future;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ComplexCallService {

    private final WebFluxReactiveFeignClient webFluxReactiveFeignClient;
    private final NormalFeignClient normalFeignClient;

    public ComplexCallService(WebFluxReactiveFeignClient webFluxReactiveFeignClient, NormalFeignClient normalFeignClient) {
        this.webFluxReactiveFeignClient = webFluxReactiveFeignClient;
        this.normalFeignClient = normalFeignClient;
    }


    public Mono<String> complexCallByReactive(long delayOne, long delayTwo) {

        Mono<String> messageForOne = webFluxReactiveFeignClient.getMessage(delayOne);
        Mono<String> messageForTwo = webFluxReactiveFeignClient.getMessage(delayTwo);

        return Mono.zip(messageForOne, messageForTwo, (one, two) -> one + "," + two);
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
}
