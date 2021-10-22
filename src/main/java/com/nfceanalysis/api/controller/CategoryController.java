package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Category;
import com.nfceanalysis.api.model.Item;
import com.nfceanalysis.api.model.LogType;
import com.nfceanalysis.api.model.TriggerLog;
import com.nfceanalysis.api.service.CategoryService;
import com.nfceanalysis.api.service.ItemService;
import com.nfceanalysis.api.service.TriggerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ItemService itemService;

    @Autowired
    TriggerLogService triggerLogService;

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

    @GetMapping("/match/{itemId}")
    public ResponseEntity<Map<String, Double>> match(@PathVariable String itemId){
        Item item = itemService.getItemById(itemId);
        return ResponseEntity.ok(categoryService.categoryMatch(item));
    }

    @PostMapping("/triggerlog")
    public ResponseEntity<?> updateTriggerLogs(){
        triggerLogService.updateAllCategoryByUser();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/triggerlog")
    public ResponseEntity<TriggerLog> getLatestTriggerLogs(){
        return ResponseEntity.ok(triggerLogService.getLatestTriggerLog(LogType.category));
    }

}
