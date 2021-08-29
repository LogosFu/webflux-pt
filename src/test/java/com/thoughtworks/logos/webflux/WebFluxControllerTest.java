package com.thoughtworks.logos.webflux;

import com.thoughtworks.logos.webflux.component.WebFluxReactiveFeignClient;
import com.thoughtworks.logos.webflux.service.ComplexCallService;
import com.thoughtworks.logos.webflux.web.WebFluxController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = WebFluxController.class)
@ImportAutoConfiguration({FeignAutoConfiguration.class})
class WebFluxControllerTest {

    @MockBean
    private WebFluxReactiveFeignClient webFluxReactiveFeignClient;

    @MockBean
    private ComplexCallService complexCallService;
    @Autowired
    private WebTestClient testClient;

    @Test
    void callReactiveFeignClient() {
        ArgumentCaptor<Long> delays = ArgumentCaptor.forClass(Long.class);
        Mockito.when(webFluxReactiveFeignClient.getMessage(1L)).thenReturn(Mono.justOrEmpty("Test"));

        testClient.get().uri("/reactive/1").exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Test");
        Mockito.verify(webFluxReactiveFeignClient).getMessage(delays.capture());
        assertThat(delays.getValue()).isEqualTo(1L);
    }

    @Test
    void should_return_message_merged_with_comma_when_reactive_complex_call() {
        //given
        ArgumentCaptor<Long> delays1 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> delays2 = ArgumentCaptor.forClass(Long.class);
        when(complexCallService.complexCallByReactive(1L, 2L)).thenReturn(Mono.justOrEmpty("Test1,Test2"));
        testClient.get().uri("/reactive/1/2").exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Test1,Test2");
        Mockito.verify(complexCallService).complexCallByReactive(delays1.capture(), delays2.capture());
        assertThat(delays1.getValue()).isEqualTo(1L);
        assertThat(delays2.getValue()).isEqualTo(2L);
    }
}
