package com.sakthi.auth.repository;

import com.sakthi.auth.model.signup.SignupRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface UserDetailsRepository extends MongoRepository<SignupRequest,String> {
    @Query("{'email':?0}")
    public SignupRequest findByEmail(String email);

}
