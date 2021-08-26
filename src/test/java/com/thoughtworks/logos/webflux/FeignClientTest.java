package com.thoughtworks.logos.webflux;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebfluxApplication.class)
public class FeignClientTest {

    @ClassRule
    public static WireMockClassRule  wiremock = new WireMockClassRule(wireMockConfig().port(18080));;
    @Autowired
    private WebFluxReactiveFeignClient webFluxReactiveFeignClient;

    @BeforeEach
    void setUp() {
        wiremock.stubFor(get(urlEqualTo("/async/delay/1")).willReturn(
                aResponse().withStatus(HttpStatus.OK.value())
                        .withBody("this is async web return")));
        wiremock.start();
    }

    @Test
    void should_return_string_when_get_message() throws IOException {

        Mono<String> message = webFluxReactiveFeignClient.getMessage();
        System.out.println(message.flux());
        StepVerifier.create(message).expectNext("this is async web return");
    }
}
