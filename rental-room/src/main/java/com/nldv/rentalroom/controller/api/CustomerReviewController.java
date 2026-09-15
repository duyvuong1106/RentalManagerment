/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.ReviewCreateRequest;
import com.nldv.rentalroom.dto.ReviewResponse;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/reviews")
public class CustomerReviewController {

    private final ReviewService reviewService;

    public CustomerReviewController(
            ReviewService reviewService) {

        this.reviewService = reviewService;
    }

    
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ReviewCreateRequest request) {

        Integer customerId
                = userDetails.getUser().getId();

        ReviewResponse response
                = reviewService.createReview(
                        customerId,
                        request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    
    @GetMapping
    public ResponseEntity<List<ReviewResponse>>
            getMyReviews(
                    @AuthenticationPrincipal CustomUserDetails userDetails) {

        Integer customerId
                = userDetails.getUser().getId();

        return ResponseEntity.ok(
                reviewService.getCustomerReviews(
                        customerId));
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse>
            getMyReview(
                    @AuthenticationPrincipal CustomUserDetails userDetails,
                    @PathVariable("id") Integer reviewId) {

        Integer customerId
                = userDetails.getUser().getId();

        return ResponseEntity.ok(
                reviewService.getCustomerReview(
                        customerId,
                        reviewId));
    }
}
