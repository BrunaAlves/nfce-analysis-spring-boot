package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Category;
import com.nfceanalysis.api.repository.CategoryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category findById(String categoryId){
        return categoryRepository.findById(new ObjectId(categoryId))
                .orElseThrow(() -> new NoSuchElementException("Category Not Found with id: " + categoryId));
    };

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
}
