package com.sakthi.auth.service.jwt;

import com.sakthi.auth.model.signup.SignupRequest;
import com.sakthi.auth.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserDetailsRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SignupRequest user = userRepository.findByEmail(email);
        if(user==null){
            throw new UsernameNotFoundException("User Not Found with username: " + email);
        }
        return UserDetailsImpl.build(user);
    }

}
