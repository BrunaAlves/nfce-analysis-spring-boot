package com.nfceanalysis.api.repository;

import com.nfceanalysis.api.model.Discount;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SpringBootApplication
public interface DiscountRepository extends MongoRepository<Discount, String> {

    Optional<Discount> findById(ObjectId id);
    Optional<List<Discount>> findByUserId(Object userId);
    Optional<Discount> findByItemId(Object userId);
}
