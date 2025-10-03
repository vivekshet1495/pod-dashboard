package com.example.poddashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "TEST ENDPOINT WORKS!";
    }
    
    @GetMapping("/")
    public String home() {
        return "HOME PAGE WORKS!";
    }
}
