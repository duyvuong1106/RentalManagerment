package com.nldv.rentalroom.controller.api;

import com.nldv.rentalroom.dto.RevenuePointResponse;
import com.nldv.rentalroom.dto.StatisticsResponse;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.StatisticsService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/landlord/statistics")
public class LandlordStatisticsController {
    private final StatisticsService service;
    public LandlordStatisticsController(StatisticsService service){this.service=service;}
    @GetMapping
    public ResponseEntity<StatisticsResponse> dashboard(@AuthenticationPrincipal CustomUserDetails u){return ResponseEntity.ok(service.landlord(u.getUser().getId()));}
    @GetMapping("/revenue")
    public ResponseEntity<List<RevenuePointResponse>> revenue(@AuthenticationPrincipal CustomUserDetails u,
            @RequestParam(defaultValue="2026") int year){return ResponseEntity.ok(service.landlordRevenue(u.getUser().getId(), year));}
}
