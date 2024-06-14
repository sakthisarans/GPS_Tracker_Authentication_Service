package com.sakthi.auth.model.signin;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SigninRequest {
    @Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",message = "Invalid Email")
    String email;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!#$%^&?])(?!.*\\s)[a-zA-Z\\d!#$%^&?]{8,}$",message = "Invalid Password")

    String password;
    LoginDevice loginDevice;
}
