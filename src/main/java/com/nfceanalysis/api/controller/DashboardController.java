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
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @GetMapping("/timeline")
    public ResponseEntity<List<Timeline>> getTimeline(@RequestParam String userId){
        return ResponseEntity.ok(dashboardService.getTimeline(userId));
    }

    @GetMapping("/piechart/perlocation")
    public ResponseEntity<PieChart> getPieGraph(@RequestParam String userId,
                                                @RequestParam Integer year,
                                                @RequestParam(required = false,defaultValue = "0") Integer month,
                                                @RequestParam(required = false,defaultValue = "0") Integer day){
        return ResponseEntity.ok(dashboardService.getPieChartPerLocation(userId, year, month, day));
    }

    @GetMapping("/piechart/category")
    public ResponseEntity<PieChart> getPieCategory(@RequestParam String userId,
                                                   @RequestParam Integer year,
                                                   @RequestParam(required = false,defaultValue = "0") Integer month,
                                                   @RequestParam(required = false,defaultValue = "0") Integer day){
        return ResponseEntity.ok(dashboardService.getPieChartCategory(userId, year, month, day));
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

    @GetMapping("/valuespermonths/{year}")
    public ResponseEntity<Chart> getValuesPerMonths(@RequestParam String userId, @PathVariable int year){
        return ResponseEntity.ok(dashboardService.getValuesPerMonths(userId, year));
    }

    @GetMapping("/icms/{year}")
    public ResponseEntity<BarLineChart> getIcms(@RequestParam String userId, @PathVariable int year){
        return ResponseEntity.ok(dashboardService.getIcms(userId, year));
    }
}
