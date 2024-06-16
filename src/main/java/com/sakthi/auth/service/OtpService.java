package com.sakthi.auth.service;

import com.sakthi.auth.model.mail.OtpRequest;
import com.sakthi.auth.model.signup.SignupRequest;
import com.sakthi.auth.model.verification.GenerateOtpRequest;
import com.sakthi.auth.model.verification.VerifyOtp;
import com.sakthi.auth.repository.UserDetailsRepository;
import com.sakthi.auth.service.generic.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final String OTP_PREFIX = "otp:";
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private static final int RATE_LIMIT = 1;
    private static final long RATE_LIMIT_PERIOD_SECONDS = 60;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    GenericService genericService;
    @Value("${authentication.otp.length}")
    private String otpLength;

    public ResponseEntity<String> generateOtp(GenerateOtpRequest generateOtpRequest) {

        SignupRequest signupRequest = userDetailsRepository.findByEmail(generateOtpRequest.getEmail());
        if (signupRequest == null || signupRequest.getAccountSettings().isActive()) {
            if (signupRequest == null) {
                return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("", HttpStatus.CONFLICT);
            }
        }
        String rateLimitKey = RATE_LIMIT_PREFIX + generateOtpRequest.getEmail();
        if (!isRateLimited(rateLimitKey)) {
            return new ResponseEntity<>("", HttpStatus.TOO_MANY_REQUESTS);
        }

        String otpKey = OTP_PREFIX + generateOtpRequest.getEmail();
        String existingOTP = redisTemplate.opsForValue().get(otpKey);
        if (existingOTP != null) {
            return new ResponseEntity<>("", HttpStatus.ALREADY_REPORTED);
        } else {
            String otpCode = generateRandomOTP(Integer.parseInt(otpLength));
            redisTemplate.opsForValue().set(otpKey, otpCode, Duration.ofMinutes(1));

            String message = "OTP for your account verification is : " + otpCode;
            genericService.sendOtpMail(
                    OtpRequest.builder().
                            to(signupRequest.getEmail()).
                            bcc("").
                            cc("").
                            subject("OTP for Account Verification").
                            body(message)
                            .build());
            return new ResponseEntity<>("", HttpStatus.OK);
        }
    }

    private String generateRandomOTP(int length) {
        char[] chars = "0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars[rnd.nextInt(chars.length)]);
        }

        return sb.toString();
    }

    private boolean isRateLimited(String rateLimitKey) {
        Long currentCount = redisTemplate.opsForValue().increment(rateLimitKey, 1);
        if (currentCount == 1) {
            redisTemplate.expire(rateLimitKey, RATE_LIMIT_PERIOD_SECONDS, TimeUnit.SECONDS);
        }
        return currentCount <= RATE_LIMIT;
    }


    public ResponseEntity<String> verifyEmailOtp(VerifyOtp verifyOtp) {

        if (verifyOtp(verifyOtp.getEmail(), verifyOtp.getOtp())) {
            SignupRequest user = userDetailsRepository.findByEmail(verifyOtp.getEmail());
            user.getAccountSettings().setActive(true);
            user.getAccountSettings().setVerified(true);
            userDetailsRepository.save(user);
            String message = "Account Activated Successfully";
            genericService.sendOtpMail(
                    OtpRequest.builder().
                            to(verifyOtp.getEmail()).
                            bcc("").
                            cc("").
                            subject("Account Activated").
                            body(message)
                            .build());
            return new ResponseEntity<>("", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Bad OTP", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean verifyOtp(String email, String otp) {
        String otpKey = OTP_PREFIX + email;
        String storedOTP = redisTemplate.opsForValue().get(otpKey);
        if (storedOTP != null && storedOTP.equals(otp)) {
            redisTemplate.delete(otpKey);
            return true;
        } else {
            return false;
        }
    }

}
