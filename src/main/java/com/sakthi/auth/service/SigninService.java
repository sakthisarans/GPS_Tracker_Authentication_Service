package com.sakthi.auth.service;

import com.sakthi.auth.customException.BannedUserException;
import com.sakthi.auth.customException.UserNotActivatedException;
import com.sakthi.auth.model.signin.SigninRequest;
import com.sakthi.auth.model.signin.SigninResponse;
import com.sakthi.auth.model.token.TokenInfo;
import com.sakthi.auth.repository.TokenValidationRepository;
import com.sakthi.auth.repository.UserDetailsRepository;
import com.sakthi.auth.service.jwt.JwtUtils;
import com.sakthi.auth.service.jwt.UserDetailsImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

import java.util.List;

@Service
@Slf4j
public class SigninService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    TokenValidationRepository tokenValidationRepository;

    @Value("${jwt.token.jwtExpirationMs}")
    private int jwtExpirationMs;


    public ResponseEntity<SigninResponse> signin(SigninRequest signinRequest) throws UserNotActivatedException, BannedUserException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication,jwtExpirationMs);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        TokenInfo.builder().userEmail(userDetails.getEmail()).build();

        TokenInfo tokenInfo=TokenInfo.builder().Token(jwt).
                userCollectionId(userDetails.getId()).
                userEmail(userDetails.getEmail()).
                dateOfCreation(new Date()).
                isActive(true).isUserCreated(false).
                message("System Created").loginDevice(signinRequest.getLoginDevice())
                .build();
            tokenValidationRepository.save(tokenInfo);
            return ResponseEntity.ok(new SigninResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
}
