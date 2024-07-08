package com.sakthi.auth.service;

import com.sakthi.auth.model.client.ClientAuthenticatedUsers;
import com.sakthi.auth.model.client.RegisterClientRequest;
import com.sakthi.auth.model.client.VerificationRequest;
import com.sakthi.auth.model.client.VerificationResponse;
import com.sakthi.auth.model.signup.SignupRequest;
import com.sakthi.auth.repository.RegisterClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    RegisterClientRepository registerClientRepository;
    public ResponseEntity<RegisterClientRequest> registerClient(RegisterClientRequest registerClientRequest){
        registerClientRequest.setUsers(new ArrayList<ClientAuthenticatedUsers>());
        if(registerClientRepository.findByTrackerid(registerClientRequest.getTrackerID())==null) {
            return ResponseEntity.ok(registerClientRepository.save(registerClientRequest));
        }else {
            return new ResponseEntity<>(null, HttpStatus.FOUND);
        }
    }
    public ResponseEntity<VerificationResponse> verifyClirnt(VerificationRequest verificationRequest){
        RegisterClientRequest validate=registerClientRepository.findByTrackeridAndPassword(verificationRequest.getUsername(), verificationRequest.getPassword());
        if(validate!=null && validate.isActive()){
            return new ResponseEntity<>(VerificationResponse.builder().isSuper(validate.isSuper()).result("allow").build(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(VerificationResponse.builder().isSuper(validate.isSuper()).result("block").build(),HttpStatus.NOT_FOUND);
        }
    }
    public void activateTracker(String id, SignupRequest signupRequest){
        RegisterClientRequest activate=registerClientRepository.findByTrackerid(id);

        List<ClientAuthenticatedUsers> cau=new ArrayList<>();

         cau.add(ClientAuthenticatedUsers.builder()
                .userId(signupRequest.getUserName())
                .email(signupRequest.getEmail()).role("ADMIN")
                .build());
        activate.setActive(true);
        activate.setUsers(cau);
        registerClientRepository.save(activate);
    }
}
