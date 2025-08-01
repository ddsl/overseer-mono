package io.github.ddsl.overseermono.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class AppController {
    @GetMapping
    public String getData() {
        return "Hello from Auth";
    }
}
