package com.thoughtworks.logos.webflux.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BlockedThreadService {

    @Async()
    public Mono<Void> blockedThread() {
        while (true) {
            Mono.just("Start").log().subscribe();
        }
    }

    @Async()
    public Mono<Void> noBlockedThread() {
        while (true) {
            Mono.just("Start").log("no blocked").subscribe();
        }
    }
}
