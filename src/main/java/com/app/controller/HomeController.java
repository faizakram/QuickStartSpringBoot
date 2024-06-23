package com.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ResponseEntity<String> home(@AuthenticationPrincipal OAuth2User oAuth2User){
        String name = oAuth2User.getAttribute("name");
        return new ResponseEntity<>("Running....." +name, HttpStatus.OK);
    }

    @GetMapping("/call/here")
    public ResponseEntity<String> callHere(@AuthenticationPrincipal OAuth2User oAuth2User){
        String name = oAuth2User.getAttribute("name");
        return new ResponseEntity<>("Running....." +name, HttpStatus.OK);
    }
}
