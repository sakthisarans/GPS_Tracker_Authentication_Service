package com.sakthi.auth.repository;

import com.sakthi.auth.model.client.RegisterClientRequest;
import com.sakthi.auth.model.token.TokenInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterClientRepository extends MongoRepository<RegisterClientRequest,String > {
    @Query("{'trackerID':?0}")
    public RegisterClientRequest findByTrackerid(String Trackerid);

    @Query("{'trackerID':?0,'trackerPassword':?1}")
    public RegisterClientRequest findByTrackeridAndPassword(String Trackerid,String password);
}
