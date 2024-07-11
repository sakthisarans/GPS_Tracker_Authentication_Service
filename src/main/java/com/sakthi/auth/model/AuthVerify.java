package com.sakthi.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter@Setter@Builder
public class AuthVerify {
    private String email;
    private List<String> role;
}
