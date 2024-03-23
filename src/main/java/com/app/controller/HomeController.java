package com.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping
    public ResponseEntity<String> home() {
        return new ResponseEntity<>("Running.....", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> forHistory(@RequestBody Map<String, Object> request) {
        return new ResponseEntity<>(request, HttpStatus.OK);
    }


    @GetMapping("exception")
    public ResponseEntity<String> exception() {
        throw new RuntimeException("Hello Exception");
    }
}
