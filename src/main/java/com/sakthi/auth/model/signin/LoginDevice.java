package com.sakthi.auth.model.signin;

import lombok.*;

@Getter@Setter
public class LoginDevice {
    private String model;
    private String platform;
    private String os;
    private String osVersion;
    private String location;

    public LoginDevice(LoginDevice loginDevice) {
        this.model=loginDevice.getModel();
        this.platform=loginDevice.getPlatform();
        this.os=loginDevice.getOs();
        this.osVersion=loginDevice.getOsVersion();
        this.location=loginDevice.getModel();
    }
    public LoginDevice(){
        this.model="Unknown";
        this.platform="Unknown";
        this.os="Unknown";
        this.osVersion="Unknown";
        this.location="Unknown";
    }
}
