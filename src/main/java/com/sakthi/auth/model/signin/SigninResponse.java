package com.sakthi.auth.model.signin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;
@Getter@Setter
public class SigninResponse {
    private String token;
    private String type = "Bearer";
    @Id@JsonIgnore
    private String id;
    private String username;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private List<String> roles;

    private boolean is2FAEnabled;

    public SigninResponse(String accessToken, String id, String username, String email, List<String> roles,boolean is2FAEnabled) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.is2FAEnabled=is2FAEnabled;
    }
}
