/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.PaymentCreateRequest;
import com.nldv.rentalroom.dto.PaymentResponse;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.PaymentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/payments")
public class CustomerPaymentController {

    private final PaymentService paymentService;

    public CustomerPaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> createPayment(
            @Valid @RequestBody PaymentCreateRequest request,
            Authentication authentication) {

        Integer customerId
                = getUserId(authentication);

        try {

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            paymentService.createPayment(
                                    customerId,
                                    request)
                    );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getPayments(
            Authentication authentication) {

        Integer customerId = getUserId(authentication);

        return ResponseEntity.ok(
                paymentService.getCustomerPayments(
                        customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(
            @PathVariable Integer id,
            Authentication authentication) {

        Integer customerId = getUserId(authentication);

        return ResponseEntity.ok(
                paymentService.getCustomerPayment(
                        customerId,
                        id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelPayment(
            @PathVariable Integer id,
            Authentication authentication) {

        Integer customerId
                = getUserId(authentication);

        try {

            return ResponseEntity.ok(
                    paymentService.cancelPayment(
                            customerId,
                            id)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    private Integer getUserId(
            Authentication authentication) {

        CustomUserDetails userDetails
                = (CustomUserDetails) authentication
                        .getPrincipal();

        return userDetails.getUser().getId();
    }
}
