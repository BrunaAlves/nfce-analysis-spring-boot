package com.nfceanalysis.api.repository;

import com.nfceanalysis.api.model.ERole;
import com.nfceanalysis.api.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(ERole name);
}
