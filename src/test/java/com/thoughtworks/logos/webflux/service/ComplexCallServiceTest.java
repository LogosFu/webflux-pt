package com.thoughtworks.logos.webflux.service;

import com.thoughtworks.logos.webflux.component.NormalFeignClient;
import com.thoughtworks.logos.webflux.component.WebFluxReactiveFeignClient;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ComplexCallServiceTest {
    @MockBean
    private WebFluxReactiveFeignClient webFluxReactiveFeignClient;
    @MockBean
    private NormalFeignClient normalFeignClient;
    @Autowired
    private ComplexCallService complexCallService;

    @Test
    void should_return_merge_message_when_reactive_complex_call() {
        //given
        ArgumentCaptor<Long> delays = ArgumentCaptor.forClass(Long.class);
        when(webFluxReactiveFeignClient.getMessage(1L)).thenReturn(Mono.justOrEmpty("Test1"));
        when(webFluxReactiveFeignClient.getMessage(2L)).thenReturn(Mono.justOrEmpty("Test2"));

        //when
        Mono<String> messageReturn = complexCallService.complexCallByReactive(1, 2);

        //then
        verify(webFluxReactiveFeignClient, times(2)).getMessage(delays.capture());
        assertThat(delays.getAllValues()).isEqualTo(asList(1L, 2L));
        StepVerifier.create(messageReturn).expectNext("Test1,Test2").verifyComplete();
    }

    @Test
    void should_return_merge_message_when_normal_complex_call() {
        //given
        ArgumentCaptor<Long> delays = ArgumentCaptor.forClass(Long.class);
        when(normalFeignClient.getMessage(1L)).thenReturn("Test1");
        when(normalFeignClient.getMessage(2L)).thenReturn("Test2");

        //when
        String messageReturn = complexCallService.complexCallByNormal(1, 2);

        //then
        verify(normalFeignClient, times(2)).getMessage(delays.capture());
        assertThat(delays.getAllValues()).isEqualTo(asList(1L, 2L));
        assertThat(messageReturn).isEqualTo("Test1,Test2");
    }

}
