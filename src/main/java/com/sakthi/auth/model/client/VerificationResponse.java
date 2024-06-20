package com.sakthi.auth.model.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class VerificationResponse {
    private String result;
    private boolean isSuper;
}
