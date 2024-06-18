package com.sakthi.auth.service.generic;

import com.sakthi.auth.model.mail.OtpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class GenericService {

    final RestTemplate restTemplate=new RestTemplate();

    @Value("${generic.service.uri}")
    String uri;

    @Value("${generic.service.email}")
    String mailEndPoint;

    public void sendOtpMail(OtpRequest otpRequest){
        URI genericUri= UriComponentsBuilder.fromHttpUrl(uri).path(mailEndPoint).build().toUri();
        restTemplate.postForEntity(genericUri,otpRequest,String.class);
    }
}
