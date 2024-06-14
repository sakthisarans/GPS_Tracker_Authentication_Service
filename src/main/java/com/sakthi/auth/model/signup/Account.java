package com.sakthi.auth.model.signup;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder@Getter@Setter
public class Account {
    private boolean isBanned;
    private boolean isActive;
    private boolean isVerified;
    private boolean is2AEnabled;
}
