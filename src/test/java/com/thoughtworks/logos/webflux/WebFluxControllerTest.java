package com.thoughtworks.logos.webflux;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = WebFluxController.class)
class WebFluxControllerTest {

    @MockBean
    private WebFluxReactiveFeignClient webFluxReactiveFeignClient;
    @Autowired
    private WebTestClient testClient;

    @Test
    void callReactiveFeignClient() {

        Mockito.when(webFluxReactiveFeignClient.getMessage()).thenReturn(Mono.justOrEmpty("Test"));

        testClient.get().uri("/feign/1").exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Test");
    }
}
