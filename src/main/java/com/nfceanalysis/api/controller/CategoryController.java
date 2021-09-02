package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Category;
import com.nfceanalysis.api.model.User;
import com.nfceanalysis.api.service.CategoryService;
import com.nfceanalysis.api.service.ItemService;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable String id){
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> findAllByUser(){
        return ResponseEntity.ok(categoryService.findByUserId());
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.create(category));
    }

    @PutMapping
    public ResponseEntity<Category> update(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.update(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        String msg = categoryService.delete(id);
        itemService.removeItemCategory(id);
        return ResponseEntity.ok(msg);
    }


}
