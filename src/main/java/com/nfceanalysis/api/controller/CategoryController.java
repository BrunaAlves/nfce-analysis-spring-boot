package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Category;
import com.nfceanalysis.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable String id){
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> findById(){
        return ResponseEntity.ok(categoryService.findAll());
    }


}
