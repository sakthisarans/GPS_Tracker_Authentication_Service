package com.sakthi.auth.model.client;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class AuthorizeClientRequest {
    private String trackerId;
    private String topic;
    private String action;
}
