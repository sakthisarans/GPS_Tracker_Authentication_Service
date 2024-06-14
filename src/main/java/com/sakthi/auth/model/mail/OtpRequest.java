package com.sakthi.auth.model.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class OtpRequest {
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
}
