package com.sakthi.auth.model.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class ClientAuthenticatedUsers {
    private String userId;
    private String email;
    private String role;
}
