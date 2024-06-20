package com.sakthi.auth.model.client;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class VerificationRequest {
    private String username;
    private String password;
}
