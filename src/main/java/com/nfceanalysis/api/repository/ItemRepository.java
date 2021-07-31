package com.nfceanalysis.api.repository;

import com.nfceanalysis.api.model.Item;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends MongoRepository<Item, String> {
    Optional<Item> findById(String id);

    List<Item> findAll();

   List<Item> findByNfce(ObjectId nfce);

}
