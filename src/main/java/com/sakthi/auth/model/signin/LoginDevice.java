package com.sakthi.auth.model.signin;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class LoginDevice {
    private String model;
    private String platform;
    private String os;
    private String osVersion;
    private String location;
}
