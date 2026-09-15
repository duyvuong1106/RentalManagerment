/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.RentalRequestResponse;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.service.RentalRequestService;
import com.nldv.rentalroom.service.UserService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/landlord/rental-requests")
public class LandlordRentalRequestController {

    private final RentalRequestService rentalRequestService;
    private final UserService userService;

    public LandlordRentalRequestController(
            RentalRequestService rentalRequestService,
            UserService userService) {

        this.rentalRequestService
                = rentalRequestService;

        this.userService
                = userService;
    }

    @GetMapping
    public ResponseEntity<?> getRequests(
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        User landlord
                = userService.findByUsername(
                        authentication.getName());

        if (landlord == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Không tìm thấy tài khoản");
        }

        return ResponseEntity.ok(
                rentalRequestService
                        .getLandlordRequests(
                                landlord.getId())
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

        User landlord
                = userService.findByUsername(
                        authentication.getName());

        try {

            return ResponseEntity.ok(
                    rentalRequestService
                            .getLandlordRequest(
                                    landlord.getId(),
                                    id)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(
            @PathVariable Integer id,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        User landlord
                = userService.findByUsername(
                        authentication.getName());

        try {

            return ResponseEntity.ok(
                    rentalRequestService
                            .approveRequest(
                                    landlord.getId(),
                                    id)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectRequest(
            @PathVariable Integer id,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        User landlord
                = userService.findByUsername(
                        authentication.getName());

        try {

            return ResponseEntity.ok(
                    rentalRequestService
                            .rejectRequest(
                                    landlord.getId(),
                                    id)
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
