package com.nldv.rentalroom.controller.api;

import com.nldv.rentalroom.dto.RevenuePointResponse;
import com.nldv.rentalroom.dto.StatisticsResponse;
import com.nldv.rentalroom.service.StatisticsService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/statistics")
public class AdminStatisticsController {
    private final StatisticsService service;
    public AdminStatisticsController(StatisticsService service){this.service=service;}
    @GetMapping
    public ResponseEntity<StatisticsResponse> dashboard(){return ResponseEntity.ok(service.admin());}
    @GetMapping("/revenue")
    public ResponseEntity<List<RevenuePointResponse>> revenue(@RequestParam(defaultValue="2026") int year){return ResponseEntity.ok(service.adminRevenue(year));}
}
