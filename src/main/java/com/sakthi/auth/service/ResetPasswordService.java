package com.sakthi.auth.service;

import com.sakthi.auth.model.mail.OtpRequest;
import com.sakthi.auth.model.resetPassword.GenerateResetPasswordRequest;
import com.sakthi.auth.model.resetPassword.ResetPasswordRequest;
import com.sakthi.auth.model.signup.SignupRequest;
import com.sakthi.auth.model.signup.SignupResponse;
import com.sakthi.auth.repository.UserDetailsRepository;
import com.sakthi.auth.service.generic.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ResetPasswordService {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    GenericService genericService;

    private static final String pwdPrefix="password:";
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private static final int RATE_LIMIT = 1;
    private static final long RATE_LIMIT_PERIOD_SECONDS = 60;
    @Value("${tracker.ui.url}")
    String uri;

    private boolean isRateLimited(String rateLimitKey) {
        Long currentCount = redisTemplate.opsForValue().increment(rateLimitKey, 1);
        if (currentCount == 1) {
            redisTemplate.expire(rateLimitKey, RATE_LIMIT_PERIOD_SECONDS, TimeUnit.SECONDS);
        }
        return currentCount <= RATE_LIMIT;
    }
    public ResponseEntity<?> generateResetLink(GenerateResetPasswordRequest request){
        SignupRequest signupRequest = userDetailsRepository.findByEmail(request.getEmail());
        if(signupRequest!=null){
            if(signupRequest.getAccountSettings().isActive()){
                String rateLimitKey = RATE_LIMIT_PREFIX + request.getEmail();
                if (!isRateLimited(rateLimitKey)) {
                    return new ResponseEntity<>("", HttpStatus.TOO_MANY_REQUESTS);
                }else{
                    String otpCode = getRandomString(15);
                    String otpKey = pwdPrefix + otpCode;
                    String existingOTP = redisTemplate.opsForValue().get(otpKey);
                    if (existingOTP != null) {
                        return new ResponseEntity<>("", HttpStatus.ALREADY_REPORTED);
                    }else {

                        redisTemplate.opsForValue().set(otpKey, request.getEmail(), Duration.ofMinutes(10));

                        URI resetLink= UriComponentsBuilder.fromHttpUrl(uri).path("/resetpassword").queryParam("resetToken",otpCode).build().toUri();

                        String message = "Your Reset Link is : " + resetLink.toString();

                        System.out.println(message);
                        genericService.sendOtpMail(
                                OtpRequest.builder().
                                        to(signupRequest.getEmail()).
                                        bcc("").
                                        cc("").
                                        subject("Password Reset").
                                        body(message)
                                        .build());
                        return new ResponseEntity<>("", HttpStatus.OK);
                    }
                }
            }else{
                return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
    }
    static String getRandomString(int i) {
        // bind the length
        byte[] bytearray;
        bytearray = new byte[256];
        String mystring;
        StringBuilder buffer;
        String theAlphaNumericS;
        new Random().nextBytes(bytearray);
        mystring = new String(bytearray, StandardCharsets.UTF_8);
        buffer = new StringBuilder();
        theAlphaNumericS = mystring.replaceAll("[^A-Z0-9]", "");
        for (int m = 0; m < theAlphaNumericS.length(); m++) {
            if (Character.isLetter(theAlphaNumericS.charAt(m)) && (i > 0)
                    || Character.isDigit(theAlphaNumericS.charAt(m)) && (i > 0)) {
                buffer.append(theAlphaNumericS.charAt(m));
                i--;
            }
        }
        return buffer.toString();
    }
    public ResponseEntity<?> resetPwd(ResetPasswordRequest request,String token){
        String email=verifyCode(token);
        if(email!=null){
            SignupRequest user=userDetailsRepository.findByEmail(email);
            if(user!=null){
                user.setPassword(encoder.encode(request.getPassword()));
                userDetailsRepository.save(user);
                return new ResponseEntity<>(SignupResponse.builder().message("User Password Updated Successful").build(), HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>("",HttpStatus.NOT_FOUND);
            }
        }else{
            return new ResponseEntity<>("",HttpStatus.NOT_FOUND);
        }
    }

    public String verifyCode( String otp) {
        String otpKey = pwdPrefix + otp;
        String storedCode = redisTemplate.opsForValue().get(otpKey);
        if (storedCode != null) {
            redisTemplate.delete(otpKey);
            return storedCode;
        } else {
            return null;
        }
    }
}
