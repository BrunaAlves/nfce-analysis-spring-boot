package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Category;
import com.nfceanalysis.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable String id){
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Category>> findAllByUser(@PathVariable String userId){
        return ResponseEntity.ok(categoryService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.create(category));
    }

    @PatchMapping
    public ResponseEntity<Category> update(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.update(category));
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String id){
        return ResponseEntity.ok(categoryService.delete(id));
    }


}
