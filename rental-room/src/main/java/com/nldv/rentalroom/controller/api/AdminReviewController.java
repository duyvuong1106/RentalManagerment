/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.ReviewResponse;
import com.nldv.rentalroom.service.ReviewService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final ReviewService reviewService;

    public AdminReviewController(
            ReviewService reviewService) {

        this.reviewService = reviewService;
    }

   
    @GetMapping
    public ResponseEntity<List<ReviewResponse>>
            getAllReviews() {

        return ResponseEntity.ok(
                reviewService.getAllReviews());
    }

    
    @PutMapping("/{id}/hide")
    public ResponseEntity<ReviewResponse>
            hideReview(
                    @PathVariable("id") Integer reviewId) {

        return ResponseEntity.ok(
                reviewService.hideReview(reviewId));
    }

    
    @PutMapping("/{id}/show")
    public ResponseEntity<ReviewResponse>
            showReview(
                    @PathVariable("id") Integer reviewId) {

        return ResponseEntity.ok(
                reviewService.showReview(reviewId));
    }
}
