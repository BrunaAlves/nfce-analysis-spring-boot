package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.Timeline;
import com.nfceanalysis.api.service.DashboardService;
import com.nfceanalysis.api.service.ItemService;
import com.nfceanalysis.api.service.NfceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {


    @Autowired
    DashboardService dashboardService;

    @GetMapping("/timeline")
    public ResponseEntity<List<Timeline>> getTimeline(@RequestParam String userId){
        return ResponseEntity.ok(dashboardService.getTimeline(userId));
    }
}
