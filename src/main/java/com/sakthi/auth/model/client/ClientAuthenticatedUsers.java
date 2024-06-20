package com.sakthi.auth.model.client;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ClientAuthenticatedUsers {
    private String userId;
    private String email;
    private String role;
    private String userName;
}
