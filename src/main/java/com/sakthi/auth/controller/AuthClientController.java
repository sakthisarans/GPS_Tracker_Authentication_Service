package com.sakthi.auth.controller;

import com.sakthi.auth.model.client.RegisterClientRequest;
import com.sakthi.auth.model.client.VerificationRequest;
import com.sakthi.auth.model.client.VerificationResponse;
import com.sakthi.auth.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tracker/auth/client")
@PreAuthorize("hasRole('ROLE_TRACKER')")
public class AuthClientController {
    @Autowired
    ClientService clientService;

    @PostMapping("/verify")
    public ResponseEntity<VerificationResponse> verify(@Valid @RequestBody VerificationRequest verificationRequest){
        return clientService.verifyClirnt(verificationRequest);
    }
    @PostMapping("/registerClient")
    public ResponseEntity<RegisterClientRequest> register(@Valid @RequestBody RegisterClientRequest registerClientRequest){
        return clientService.registerClient(registerClientRequest);
    }
}
