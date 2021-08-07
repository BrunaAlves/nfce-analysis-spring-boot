package com.nfceanalysis.api.repository;

import com.nfceanalysis.api.model.Nfce;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SpringBootApplication
public interface NfceRepository extends MongoRepository<Nfce, String> {

    Optional<Nfce> findById(String id);

    Optional<List<Nfce>> findByUser(ObjectId user);

    @Query("{ 'issuanceDate' : { '$regex' : ?0 , $options: 'i'}}")
    List<Nfce> searchByIssuanceDate(String issuanceDate, ObjectId user);
}
