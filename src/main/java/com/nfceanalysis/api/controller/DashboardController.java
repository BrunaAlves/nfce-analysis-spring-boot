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
    public ResponseEntity<List<Timeline>> getTimeline(){
        return ResponseEntity.ok(dashboardService.getTimeline());
    }

    @GetMapping("/piechart/perlocation")
    public ResponseEntity<PieChart> getPieGraph(@RequestParam Integer year,
                                                @RequestParam(required = false,defaultValue = "0") Integer month,
                                                @RequestParam(required = false,defaultValue = "0") Integer day){
        return ResponseEntity.ok(dashboardService.getPieChartPerLocation(year, month, day));
    }

    @GetMapping("/piechart/category")
    public ResponseEntity<PieChart> getPieCategory(@RequestParam Integer year,
                                                   @RequestParam(required = false,defaultValue = "0") Integer month,
                                                   @RequestParam(required = false,defaultValue = "0") Integer day){
        return ResponseEntity.ok(dashboardService.getPieChartCategory(year, month, day));
    }

    @GetMapping("/totalcurrentyear")
    public ResponseEntity<Float> getTotalSpentInTheYear(){
        return ResponseEntity.ok(dashboardService.getTotalSpentInTheYear());
    }

    @GetMapping("/totallastyear")
    public ResponseEntity<Float> getTotalSpentInTheLastYear(){
        return ResponseEntity.ok(dashboardService.getTotalSpentInTheLastYear());
    }

    @GetMapping("/totalcurrentmonth")
    public ResponseEntity<Float> getTotalSpentInTheCurrentMonth(){
        return ResponseEntity.ok(dashboardService.getTotalSpentInTheCurrentMonth());
    }

    @GetMapping("/totallastmonth")
    public ResponseEntity<Float> getTotalSpentInTheLastMonth(){
        return ResponseEntity.ok(dashboardService.getTotalSpentInTheLastMonth());
    }

    @GetMapping("/valuespermonths")
    public ResponseEntity<Chart> getValuesPerMonths(@RequestParam int year){
        return ResponseEntity.ok(dashboardService.getValuesPerMonths(year));
    }

    @GetMapping("/icms")
    public ResponseEntity<BarLineChart> getIcms(@RequestParam int year){
        return ResponseEntity.ok(dashboardService.getIcms(year));
    }
}
