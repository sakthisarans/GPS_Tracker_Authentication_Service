package com.sakthi.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping {
    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
 }
