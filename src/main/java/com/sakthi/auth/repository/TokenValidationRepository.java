package com.sakthi.auth.repository;

import com.sakthi.auth.model.token.TokenInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenValidationRepository extends MongoRepository<TokenInfo,String> {

}
