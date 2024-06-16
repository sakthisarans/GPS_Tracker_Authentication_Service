package com.sakthi.auth.model.signout;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SignoutRequest {
    @NotBlank(message = "token is mandatory")
    private String token;
}
