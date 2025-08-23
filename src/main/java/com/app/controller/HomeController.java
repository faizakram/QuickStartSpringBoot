package com.app.controller;

import com.app.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {


    @GetMapping
    public ResponseEntity<String> home() {
        return new ResponseEntity<>("Running.....", HttpStatus.OK);
    }

    @GetMapping("exception")
    public ResponseEntity<String> exceptionTest(@RequestParam(required = false) Integer id) {
        if (id == null) {
            throw new CustomException("E0001", "Data Not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Running.....", HttpStatus.OK);
    }

}
