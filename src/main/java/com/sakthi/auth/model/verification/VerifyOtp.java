package com.sakthi.auth.model.verification;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class VerifyOtp {
    @Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$",message = "Invalid Email")
    private String email;
    @Pattern(regexp = "[0-9]{6}",message = "invalid OTP")
    private String otp;
}
