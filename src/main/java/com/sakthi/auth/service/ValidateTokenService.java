package com.sakthi.auth.service;

import com.sakthi.auth.model.AuthVerify;
import com.sakthi.auth.service.jwt.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ValidateTokenService {

    public ResponseEntity<AuthVerify> getDataFromToken(){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
            Collection<? extends GrantedAuthority> roles= user.getAuthorities();
            List<String> roleList=new ArrayList<>();
            roles.forEach(x->{
                roleList.add(x.getAuthority());
            });
            return new ResponseEntity<>(AuthVerify.builder().email(user.getEmail()).role(roleList).build(), HttpStatus.OK);
    }
}
