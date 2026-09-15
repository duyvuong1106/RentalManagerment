/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.PaymentResponse;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.PaymentService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/landlord/payments")
public class LandlordPaymentController {

    private final PaymentService paymentService;

    public LandlordPaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getPayments(
            Authentication authentication) {

        Integer landlordId = getUserId(authentication);

        return ResponseEntity.ok(
                paymentService.getLandlordPayments(
                        landlordId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(
            @PathVariable Integer id,
            Authentication authentication) {

        Integer landlordId = getUserId(authentication);

        return ResponseEntity.ok(
                paymentService.getLandlordPayment(
                        landlordId,
                        id));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmPayment(
            @PathVariable Integer id,
            Authentication authentication) {

        Integer landlordId
                = getUserId(authentication);

        try {

            return ResponseEntity.ok(
                    paymentService.confirmPayment(
                            landlordId,
                            id)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<PaymentResponse> rejectPayment(
            @PathVariable Integer id,
            Authentication authentication) {

        Integer landlordId = getUserId(authentication);

        return ResponseEntity.ok(
                paymentService.rejectPayment(
                        landlordId,
                        id));
    }

    private Integer getUserId(
            Authentication authentication) {

        CustomUserDetails userDetails
                = (CustomUserDetails) authentication
                        .getPrincipal();

        return userDetails.getUser().getId();
    }
}
