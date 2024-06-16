package com.sakthi.auth.controller;

import com.sakthi.auth.customException.BannedUserException;
import com.sakthi.auth.customException.UserNotActivatedException;
import com.sakthi.auth.model.signin.SigninRequest;
import com.sakthi.auth.model.signin.SigninResponse;
import com.sakthi.auth.model.signup.SignupRequest;
import com.sakthi.auth.model.signup.SignupResponse;
import com.sakthi.auth.model.verification.GenerateOtpRequest;
import com.sakthi.auth.model.verification.VerifyOtp;
import com.sakthi.auth.service.OtpService;
import com.sakthi.auth.service.SigninService;
import com.sakthi.auth.service.SignupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracker/auth/user")
public class AuthUserController {
    @Autowired
    SignupService signupService;
    @Autowired
    SigninService signinService;
    @Autowired
    OtpService generateOtpService;
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> singup(@Valid @RequestBody SignupRequest signupRequest){
        return signupService.signup(signupRequest);
    }
    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> singin(@RequestBody SigninRequest signinRequest) throws UserNotActivatedException, BannedUserException {
        return signinService.signin(signinRequest);
    }

    @PostMapping("/accountverify/generateotp")
    public ResponseEntity<String> generateOTP(@Valid @RequestBody GenerateOtpRequest otpRequest){
        return generateOtpService.generateOtp(otpRequest);
    }
    @PostMapping("/accountverify/verifyotp")
    public ResponseEntity<String> verifyOTP(@Valid @RequestBody VerifyOtp verifyOtp){
        return generateOtpService.verifyEmailOtp(verifyOtp);
    }
}
