package com.sakthi.auth.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sakthi.auth.model.signout.SignoutRequest;
import com.sakthi.auth.service.SigninService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracker/user")
public class UserController {
    @Autowired
    SigninService signinService;

    @PostMapping("/signout")
    public ResponseEntity<String> signout(@Valid @RequestBody SignoutRequest token){
        return signinService.signout(token);
    }
    @GetMapping("/test")
    public String test(){
        return "OK";
    }
}
