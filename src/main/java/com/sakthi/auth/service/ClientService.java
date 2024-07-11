package com.sakthi.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakthi.auth.model.client.*;
import com.sakthi.auth.model.signup.SignupRequest;
import com.sakthi.auth.repository.RegisterClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
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
            return new ResponseEntity<>(VerificationResponse.builder().isSuper(false).result("block").build(),HttpStatus.NOT_FOUND);
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

    public ResponseEntity<AuthorizeClientResponse> authorize(AuthorizeClientRequest authorizeClient){
        try{
            ObjectMapper obj=new ObjectMapper();
            log.debug(obj.writeValueAsString(authorizeClient));
        }catch (Exception ex){

        }
        if(Objects.equals(authorizeClient.getAction(), "subscribe")){
            if(authorizeClient.getTopic().contains(authorizeClient.getTrackerId())){
                return new ResponseEntity<>(new AuthorizeClientResponse("allow"),HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new AuthorizeClientResponse("deny"),HttpStatus.OK);
            }
        } else if (Objects.equals(authorizeClient.getAction(), "publish")) {
            if(authorizeClient.getTopic().contains(authorizeClient.getTrackerId())){
                return new ResponseEntity<>(new AuthorizeClientResponse("allow"),HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new AuthorizeClientResponse("deny"),HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>(new AuthorizeClientResponse("deny"),HttpStatus.OK);
        }
    }
}
