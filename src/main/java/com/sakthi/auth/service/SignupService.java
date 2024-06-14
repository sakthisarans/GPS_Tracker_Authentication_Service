package com.sakthi.auth.service;

import com.sakthi.auth.model.jwt.ERole;
import com.sakthi.auth.model.jwt.Role;
import com.sakthi.auth.model.signup.SignupRequest;
import com.sakthi.auth.model.signup.Account;
import com.sakthi.auth.model.signup.SignupResponse;
import com.sakthi.auth.repository.RoleRepository;
import com.sakthi.auth.repository.UserDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SignupService {


    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<SignupResponse> signup(SignupRequest signupRequest){
        log.debug(signupRequest.getPassword());
        if(userDetailsRepository.findByEmail(signupRequest.getEmail())==null) {

            signupRequest.setPassword(encoder.encode(signupRequest.getPassword()));
            List<String> strRoles = signupRequest.getRoleList();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        case "tracker":
                            Role trackerRole = roleRepository.findByName(ERole.ROLE_TRACKER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(trackerRole);

                            break;
                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                    }
                });
            }
            signupRequest.setDateOfCreation(new Date());
            signupRequest.setAccountDetail(Account.builder().isBanned(false).isActive(false).is2AEnabled(false).isVerified(false).build());
            signupRequest.setRoles((roles));

            userDetailsRepository.save(signupRequest);
            return new ResponseEntity<>(SignupResponse.builder().message("User Created Successful").build(), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(SignupResponse.builder().message("User Already Exists").build(), HttpStatus.FOUND);
        }
    }
}
