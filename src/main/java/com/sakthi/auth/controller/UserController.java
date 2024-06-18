package com.sakthi.auth.controller;

import com.sakthi.auth.model.signin.SigninRequest;
import com.sakthi.auth.model.signout.SignoutRequest;
import com.sakthi.auth.model.token.TokenInfo;
import com.sakthi.auth.service.SigninService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracker/user")
public class UserController {
    @Autowired
    SigninService signinService;

    @PostMapping("/signout")
    public ResponseEntity<String> signout(@Valid @RequestBody SignoutRequest token){
        return signinService.signout(token);
    }

    @GetMapping("/gettoken")
    public ResponseEntity<List<TokenInfo>> gettoken(@RequestParam(required = true,defaultValue = "false") boolean userCreatedTokens){
        return signinService.getToken(userCreatedTokens);
    }

    @DeleteMapping("/signoutall")
    public ResponseEntity<String> signoutAll(@Valid @RequestBody SigninRequest signoutAllRequest){
        return signinService.signoutAll(signoutAllRequest);
    }

    @GetMapping("/test")
    public String test(){
        return "OK";
    }
}
