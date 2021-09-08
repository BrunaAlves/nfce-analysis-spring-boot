package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Discount;
import com.nfceanalysis.api.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/all")
    public ResponseEntity<List<Discount>> findAllByUser(){
        return ResponseEntity.ok(discountService.findByUserId());
    }

    @PostMapping("/{itemId}")
    public ResponseEntity<Discount> create(@PathVariable String itemId, @RequestBody Discount discount){
        return ResponseEntity.ok(discountService.create(itemId, discount));
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String id){
        return ResponseEntity.ok(discountService.delete(id));
    }
}
