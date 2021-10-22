package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Acquisition;
import com.nfceanalysis.api.model.LogType;
import com.nfceanalysis.api.model.TriggerLog;
import com.nfceanalysis.api.service.AcquisitionService;
import com.nfceanalysis.api.service.TriggerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/acquisition")
public class AcquisitionController {

    @Autowired
    AcquisitionService acquisitionService;

    @Autowired
    TriggerLogService triggerLogService;

    @GetMapping("/{id}")
    public ResponseEntity<Acquisition> findById(@PathVariable String id){
        return ResponseEntity.ok(acquisitionService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Acquisition>> findAll(){
        return ResponseEntity.ok(acquisitionService.findAll());
    }

    @PostMapping
    public ResponseEntity<Acquisition> create(@RequestBody Acquisition acquisition){
        return ResponseEntity.ok(acquisitionService.create(acquisition));
    }

    @PutMapping
    public ResponseEntity<Acquisition> update(@RequestBody Acquisition acquisition){
        return ResponseEntity.ok(acquisitionService.update(acquisition));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        acquisitionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/triggerlog")
    public ResponseEntity<?> updateTriggerLogs(){
        triggerLogService.updateAllAcquisitionByUser();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/triggerlog")
    public ResponseEntity<TriggerLog> getLatestTriggerLogs(){
        return ResponseEntity.ok(triggerLogService.getLatestTriggerLog(LogType.acquisition));
    }
}
