package com.sakthi.auth.service;

import com.sakthi.auth.model.client.RegisterClientRequest;
import com.sakthi.auth.model.signup.SignupRequest;
import com.sakthi.auth.model.verification.ValidateDataResponse;
import com.sakthi.auth.repository.RegisterClientRepository;
import com.sakthi.auth.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidateDataService {
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    RegisterClientRepository registerClientRepository;

    public ResponseEntity<ValidateDataResponse> validate(String content,String value){
        if(content.equals("email")){
            SignupRequest data=userDetailsRepository.findByEmail(value);
            if(data!=null){
                if(!data.getAccountSettings().isActive()){
                    return new ResponseEntity<>(new ValidateDataResponse(true), HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(new ValidateDataResponse(false), HttpStatus.OK);
                }
            }else{
                return new ResponseEntity<>(new ValidateDataResponse(true), HttpStatus.OK);
            }
        } else if (content.equals("trackerid")) {
            RegisterClientRequest data=registerClientRepository.findByTrackerid(value);
            if(data!=null){
                if(!data.isActive()){
                    return new ResponseEntity<>(new ValidateDataResponse(true), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(new ValidateDataResponse(false), HttpStatus.OK);
                }
            }else{
                return new ResponseEntity<>(new ValidateDataResponse(false), HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>(new ValidateDataResponse(false), HttpStatus.BAD_REQUEST);
        }
    }
}
