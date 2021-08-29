package com.thoughtworks.logos.webflux;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "mocker-service",url = "mocker-service:18086")
public interface NormalFeignClient {
    @GetMapping("/async/delay/{param}")
    String getMessage(@PathVariable Long param);
}
