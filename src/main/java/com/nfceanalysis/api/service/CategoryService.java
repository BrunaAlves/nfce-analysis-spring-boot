package com.nfceanalysis.api.service;

import com.nfceanalysis.api.model.Category;
import com.nfceanalysis.api.model.User;
import com.nfceanalysis.api.repository.CategoryRepository;
import com.nfceanalysis.api.security.service.UserDetailsService;
import com.nfceanalysis.api.security.service.UserDetailsImpl;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    private final UserDetailsService userDetailsService;

    public CategoryService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    public Category findById(String categoryId){
        return categoryRepository.findById(new ObjectId(categoryId))
                .orElseThrow(() -> new NoSuchElementException("Category Not Found with id: " + categoryId));
    }

    public List<Category> findByUserId(){
        return categoryRepository.findByUserId(new ObjectId(userDetailsService.getUserId()))
                .orElseThrow(() -> new NoSuchElementException("Category Not Found for the user"));
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
