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
    }

    public List<Category> findByUserId(String userId){
        return categoryRepository.findByUserId(new ObjectId(userId))
                .orElseThrow(() -> new NoSuchElementException("Category Not Found for userId: " + userId));
    }

    public Category create(Category category){
        return categoryRepository.save(category);
    }

    public Category update(Category category){
        return categoryRepository.save(category);
    }

    public String delete(String id){
        categoryRepository.deleteById(id);
        return "Successfully delete category with id: " + id;
    }
}
