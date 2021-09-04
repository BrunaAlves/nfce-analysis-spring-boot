package com.nfceanalysis.api.repository;

import com.nfceanalysis.api.model.Acquisition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SpringBootApplication
public interface AcquisitionRepository extends MongoRepository<Acquisition, String> {

    Optional<List<Acquisition>> findByUserId(Object userId);
}
