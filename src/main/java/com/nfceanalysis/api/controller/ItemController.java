package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Item;
import com.nfceanalysis.api.repository.ItemRepository;
import com.nfceanalysis.api.security.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemsById(@PathVariable String id){
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @GetMapping("nfce/{nfceId}")
    public ResponseEntity<List<Item>> getItemsByNfceId(@PathVariable String nfceId){
        return ResponseEntity.ok(itemService.getItemByNfce(nfceId));
    }


}
