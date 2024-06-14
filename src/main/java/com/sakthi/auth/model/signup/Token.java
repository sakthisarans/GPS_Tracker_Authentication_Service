package com.sakthi.auth.model.signup;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter@Setter
public class Token {

    private Set<String> roles = new HashSet<>();
}
