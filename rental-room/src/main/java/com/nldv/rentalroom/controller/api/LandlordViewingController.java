package com.nldv.rentalroom.controller.api;

import com.nldv.rentalroom.dto.ViewingAppointmentResponse;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.ViewingAppointmentService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/landlord/viewings")
public class LandlordViewingController {
    private final ViewingAppointmentService service;
    public LandlordViewingController(ViewingAppointmentService service) { this.service = service; }
    @GetMapping
    public ResponseEntity<List<ViewingAppointmentResponse>> list(@AuthenticationPrincipal CustomUserDetails u) { return ResponseEntity.ok(service.getLandlordAppointments(u.getUser().getId())); }
    @GetMapping("/{id}")
    public ResponseEntity<ViewingAppointmentResponse> detail(@AuthenticationPrincipal CustomUserDetails u, @PathVariable Integer id) { return ResponseEntity.ok(service.getLandlordAppointment(u.getUser().getId(), id)); }
    @PutMapping("/{id}/approve")
    public ResponseEntity<ViewingAppointmentResponse> approve(@AuthenticationPrincipal CustomUserDetails u, @PathVariable Integer id) { return ResponseEntity.ok(service.approve(u.getUser().getId(), id)); }
    @PutMapping("/{id}/reject")
    public ResponseEntity<ViewingAppointmentResponse> reject(@AuthenticationPrincipal CustomUserDetails u, @PathVariable Integer id) { return ResponseEntity.ok(service.reject(u.getUser().getId(), id)); }
    @PutMapping("/{id}/complete")
    public ResponseEntity<ViewingAppointmentResponse> complete(@AuthenticationPrincipal CustomUserDetails u, @PathVariable Integer id) { return ResponseEntity.ok(service.complete(u.getUser().getId(), id)); }
}
