package com.sakthi.auth.repository;

import com.sakthi.auth.model.jwt.ERole;
import com.sakthi.auth.model.jwt.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
