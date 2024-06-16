package com.sakthi.auth.scheduler;

import com.sakthi.auth.model.token.TokenInfo;
import com.sakthi.auth.repository.TokenValidationRepository;
import com.sakthi.auth.service.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
@Configuration
public class ExpiredTokenHandler {
    @Autowired
    TokenValidationRepository tokenValidationRepository;
    @Autowired
    JwtUtils jwtUtils;

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(fixedRate = 1000)
    private void expiredTokenHandler(){
        List<TokenInfo> listForDeletion=new ArrayList<>();
        tokenValidationRepository.findAll().forEach(x->{
            if((!x.isActive())&&(!jwtUtils.validateJwtToken(x.getToken()))){
                listForDeletion.add(x);
            }
        });
        tokenValidationRepository.deleteAll(listForDeletion);
    }
}
