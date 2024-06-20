package com.sakthi.auth.service;

import com.sakthi.auth.model.client.RegisterClientRequest;
import com.sakthi.auth.model.jwt.ERole;
import com.sakthi.auth.model.jwt.Role;
import com.sakthi.auth.model.signup.SignupRequest;
import com.sakthi.auth.model.signup.Account;
import com.sakthi.auth.model.signup.SignupResponse;
import com.sakthi.auth.repository.RegisterClientRepository;
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
public class SignupService {
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    ClientService clientService;

    @Autowired
    RegisterClientRepository registerClientRepository;

    public ResponseEntity<SignupResponse> signup(SignupRequest signupRequest){
        if(!signupRequest.getTrackerList().isEmpty()){
            RegisterClientRequest tracker=registerClientRepository.findByTrackerid(signupRequest.getTrackerList().get(0).getTrackerID());
            if(signupRequest.getTrackerList().size()>1){
                return new ResponseEntity<>(null,HttpStatus.TOO_MANY_REQUESTS);
            }
            else if(tracker==null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            else if(tracker.isActive()){
                return new ResponseEntity<>(null,HttpStatus.ALREADY_REPORTED);
            }
            else {
                clientService.activateTracker(signupRequest.getTrackerList().get(0).getTrackerID());
            }
        }else{
            if(!signupRequest.getRoleList().contains("tracker")) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }else {
                signupRequest.setTrackerList(new ArrayList<>());
            }
        }
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
            signupRequest.setAccountSettings(Account.builder().isBanned(false).isActive(false).is2AEnabled(false).isVerified(false).build());
            signupRequest.setRoles((roles));
            userDetailsRepository.save(signupRequest);
            return new ResponseEntity<>(SignupResponse.builder().message("User Created Successful").build(), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(SignupResponse.builder().message("User Already Exists").build(), HttpStatus.FOUND);
        }
    }
}
