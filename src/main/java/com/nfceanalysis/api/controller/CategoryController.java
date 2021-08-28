package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Category;
import com.nfceanalysis.api.service.CategoryService;
import com.nfceanalysis.api.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        String msg = categoryService.delete(id);
        itemService.removeItemCategory(id);
        return ResponseEntity.ok(msg);
    }


}
