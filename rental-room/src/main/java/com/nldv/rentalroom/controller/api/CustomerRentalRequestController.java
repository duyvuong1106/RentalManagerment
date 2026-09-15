/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.RentalRequestCreateRequest;
import com.nldv.rentalroom.dto.RentalRequestResponse;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.service.RentalRequestService;
import com.nldv.rentalroom.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/rental-requests")
public class CustomerRentalRequestController {

    private final RentalRequestService rentalRequestService;
    private final UserService userService;

    public CustomerRentalRequestController(
            RentalRequestService rentalRequestService,
            UserService userService) {

        this.rentalRequestService
                = rentalRequestService;

        this.userService
                = userService;
    }

    @PostMapping
    public ResponseEntity<?> createRequest(
            @Valid @RequestBody RentalRequestCreateRequest request,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        User customer
                = userService.findByUsername(
                        authentication.getName());

        if (customer == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Không tìm thấy tài khoản");
        }

        try {

            RentalRequestResponse response
                    = rentalRequestService.createRequest(
                            customer.getId(),
                            request
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getMyRequests(
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        User customer
                = userService.findByUsername(
                        authentication.getName());

        if (customer == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Không tìm thấy tài khoản");
        }

        return ResponseEntity.ok(
                rentalRequestService
                        .getCustomerRequests(
                                customer.getId())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequest(
            @PathVariable Integer id,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        User customer
                = userService.findByUsername(
                        authentication.getName());

        try {

            return ResponseEntity.ok(
                    rentalRequestService
                            .getCustomerRequest(
                                    customer.getId(),
                                    id)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRequest(
            @PathVariable Integer id,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        User customer
                = userService.findByUsername(
                        authentication.getName());

        try {

            return ResponseEntity.ok(
                    rentalRequestService
                            .cancelRequest(
                                    customer.getId(),
                                    id)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
