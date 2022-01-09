package com.thoughtworks.logos.webflux.common;

import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;
import reactor.util.context.Context;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class MdcContextLifterConfiguration {
    public static final String MDC_CONTEXT_REACTOR_KEY = "MDC-CONTEXT-REACTOR";

    @PostConstruct
    public void installContextHook() {
        Hooks.onEachOperator(MDC_CONTEXT_REACTOR_KEY, Operators.lift(($, subscriber) -> new MdcContextLifter<>(subscriber)));
    }

    @PreDestroy
    public void uninstallContextHook() {
        Hooks.resetOnEachOperator(MDC_CONTEXT_REACTOR_KEY);
    }

    public static class MdcContextLifter<T> implements CoreSubscriber<T> {
        private final CoreSubscriber<T> coreSubscriber;


        public MdcContextLifter(CoreSubscriber<T> coreSubscriber) {
            this.coreSubscriber = coreSubscriber;
        }

        @Override
        public Context currentContext() {
            return coreSubscriber.currentContext();
        }

        @Override
        public void onSubscribe(Subscription s) {
            copyContextToMdc(coreSubscriber.currentContext());
            coreSubscriber.onSubscribe(s);
        }

        @Override
        public void onNext(T t) {
            coreSubscriber.onNext(t);
        }

        @Override
        public void onError(Throwable t) {
            coreSubscriber.onError(t);
        }

        @Override
        public void onComplete() {
            coreSubscriber.onComplete();
        }

        private void copyContextToMdc(Context context) {
            MDC.clear();
            if (!context.isEmpty()) {
                Map<String, String> newContext = context.stream()
                        .filter(entry -> entry.getKey() instanceof String && entry.getValue() instanceof String)
                        .collect(Collectors.toMap(entry -> (String) entry.getKey(), entry -> (String) entry.getValue()));
                MDC.setContextMap(newContext);
            }
        }
    }
}
