package com.nfceanalysis.api.repository;

import com.nfceanalysis.api.model.Item;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SpringBootApplication
public interface ItemRepository extends MongoRepository<Item, String> {
    Optional<Item> findById(String id);

    List<Item> findAll();

    Optional<List<Item>> findByItemCode(String itemCode);

   List<Item> findByNfce(ObjectId nfce);

   List<Item> findByNfceAndCategoryIdNotNull(ObjectId nfce);

   List<Item> findByCategoryId(ObjectId categoryId);

}
