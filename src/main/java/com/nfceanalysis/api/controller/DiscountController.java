package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Discount;
import com.nfceanalysis.api.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @GetMapping("/{id}")
    public ResponseEntity<Discount> findById(@PathVariable String id){
        return ResponseEntity.ok(discountService.findById(id));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Discount>> findAllByUser(@PathVariable String userId){
        return ResponseEntity.ok(discountService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Discount> create(@RequestBody Discount Discount){
        return ResponseEntity.ok(discountService.create(Discount));
    }

    @PatchMapping
    public ResponseEntity<Discount> update(@RequestBody Discount discount){
        return ResponseEntity.ok(discountService.update(discount));
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String id){
        return ResponseEntity.ok(discountService.delete(id));
    }
}
