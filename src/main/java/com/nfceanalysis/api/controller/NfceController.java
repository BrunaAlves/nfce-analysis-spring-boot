package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Nfce;
import com.nfceanalysis.api.service.NfceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nfce")
public class NfceController {

    @Autowired
    NfceService nfceService;

    @GetMapping("/{id}")
    public ResponseEntity<Nfce> findById(@PathVariable String id){
        return ResponseEntity.ok(nfceService.getNfceById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Nfce>> findAllByUser(@PathVariable String userId){
        return ResponseEntity.ok(nfceService.getNfceByUser(userId));
    }

}
