package com.nfceanalysis.api.repository;

import com.nfceanalysis.api.model.Category;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SpringBootApplication
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findById(ObjectId id);

    Optional<List<Category>> findByUserId(Object userId);
}
