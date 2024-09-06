package com.sakthi.auth.controller;

import com.sakthi.auth.customException.BannedUserException;
import com.sakthi.auth.customException.UserNotActivatedException;
import com.sakthi.auth.model.resetPassword.GenerateResetPasswordRequest;
import com.sakthi.auth.model.resetPassword.ResetPasswordRequest;
import com.sakthi.auth.model.signin.SigninRequest;
import com.sakthi.auth.model.signin.SigninResponse;
import com.sakthi.auth.model.signup.SignupRequest;
import com.sakthi.auth.model.signup.SignupResponse;
import com.sakthi.auth.model.verification.GenerateOtpRequest;
import com.sakthi.auth.model.verification.ValidateDataResponse;
import com.sakthi.auth.model.verification.VerifyOtp;
import com.sakthi.auth.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    ResetPasswordService resetPasswordService;
    @Autowired
    ValidateDataService validateDataService;
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
    @GetMapping("/data/{content}/{value}")
    public ResponseEntity<ValidateDataResponse> validate(@PathVariable String content,@PathVariable String value){
        return validateDataService.validate(content, value);
    }
    @PostMapping("/generateresetlink")
    public ResponseEntity<?> generateResetLink(@RequestBody GenerateResetPasswordRequest request){
        return resetPasswordService.generateResetLink(request);
    }
    @PutMapping("/resetpassword")
    public ResponseEntity<?> generateReset(@RequestBody ResetPasswordRequest request,@RequestParam("resetToken")String token){
        return resetPasswordService.resetPwd(request,token);
    }
}
