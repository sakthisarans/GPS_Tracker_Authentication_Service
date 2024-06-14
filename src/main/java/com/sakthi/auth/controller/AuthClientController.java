package com.sakthi.auth.controller;

import com.sakthi.auth.model.signup.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tracker/auth/client")
public class AuthClientController {
    @PostMapping("/verify")
    public ResponseEntity<?> singup(){
        return null;
    }
}
