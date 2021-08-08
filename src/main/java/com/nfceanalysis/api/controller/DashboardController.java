package com.nfceanalysis.api.controller;

import com.nfceanalysis.api.model.BarLineChart;
import com.nfceanalysis.api.model.Chart;
import com.nfceanalysis.api.model.PieChart;
import com.nfceanalysis.api.model.Timeline;
import com.nfceanalysis.api.service.DashboardService;
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

    @GetMapping("/piechart")
    public ResponseEntity<PieChart> getPieGraph(@RequestParam String userId){
        return ResponseEntity.ok(dashboardService.getPieChart(userId));
    }

    @GetMapping("/totalcurrentyear")
    public ResponseEntity<Float> getTotalSpentInTheYear(@RequestParam String userId){
        return ResponseEntity.ok(dashboardService.getTotalSpentInTheYear(userId));
    }

    @GetMapping("/totallastyear")
    public ResponseEntity<Float> getTotalSpentInTheLastYear(@RequestParam String userId){
        return ResponseEntity.ok(dashboardService.getTotalSpentInTheLastYear(userId));
    }

    @GetMapping("/totalcurrentmonth")
    public ResponseEntity<Float> getTotalSpentInTheCurrentMonth(@RequestParam String userId){
        return ResponseEntity.ok(dashboardService.getTotalSpentInTheCurrentMonth(userId));
    }

    @GetMapping("/totallastmonth")
    public ResponseEntity<Float> getTotalSpentInTheLastMonth(@RequestParam String userId){
        return ResponseEntity.ok(dashboardService.getTotalSpentInTheLastMonth(userId));
    }

    @GetMapping("/valuespermonths")
    public ResponseEntity<Chart> getValuesPerMonths(@RequestParam String userId){
        return ResponseEntity.ok(dashboardService.getValuesPerMonths(userId));
    }

    @GetMapping("/icms")
    public ResponseEntity<BarLineChart> getIcms(@RequestParam String userId){
        return ResponseEntity.ok(dashboardService.getIcms(userId));
    }
}
