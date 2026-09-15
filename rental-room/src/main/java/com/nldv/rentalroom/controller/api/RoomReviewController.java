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
@RequestMapping("/api/rooms")
public class RoomReviewController {

    private final ReviewService reviewService;

    public RoomReviewController(
            ReviewService reviewService) {

        this.reviewService = reviewService;
    }

    
    @GetMapping("/{roomId}/reviews")
    public ResponseEntity<List<ReviewResponse>>
            getRoomReviews(
                    @PathVariable("roomId") Integer roomId) {

        return ResponseEntity.ok(
                reviewService.getRoomReviews(roomId));
    }
}
