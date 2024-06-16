package com.sakthi.auth.repository;

import com.sakthi.auth.model.token.TokenInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenValidationRepository extends MongoRepository<TokenInfo,String> {
    @Query("{'Token':?0}")
public TokenInfo findByToken(String token);
}
