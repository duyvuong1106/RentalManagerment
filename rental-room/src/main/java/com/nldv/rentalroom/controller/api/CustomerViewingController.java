package com.nldv.rentalroom.controller.api;

import com.nldv.rentalroom.dto.ViewingAppointmentCreateRequest;
import com.nldv.rentalroom.dto.ViewingAppointmentResponse;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.ViewingAppointmentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/viewings")
public class CustomerViewingController {
    private final ViewingAppointmentService service;
    public CustomerViewingController(ViewingAppointmentService service) { this.service = service; }
    @PostMapping
    public ResponseEntity<ViewingAppointmentResponse> create(@AuthenticationPrincipal CustomUserDetails u, @Valid @RequestBody ViewingAppointmentCreateRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(u.getUser().getId(), r));
    }
    @GetMapping
    public ResponseEntity<List<ViewingAppointmentResponse>> list(@AuthenticationPrincipal CustomUserDetails u) { return ResponseEntity.ok(service.getCustomerAppointments(u.getUser().getId())); }
    @GetMapping("/{id}")
    public ResponseEntity<ViewingAppointmentResponse> detail(@AuthenticationPrincipal CustomUserDetails u, @PathVariable Integer id) { return ResponseEntity.ok(service.getCustomerAppointment(u.getUser().getId(), id)); }
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ViewingAppointmentResponse> cancel(@AuthenticationPrincipal CustomUserDetails u, @PathVariable Integer id) { return ResponseEntity.ok(service.cancelByCustomer(u.getUser().getId(), id)); }
}
