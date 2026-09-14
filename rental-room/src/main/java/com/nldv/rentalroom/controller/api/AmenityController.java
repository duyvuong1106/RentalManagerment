/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.dto.AmenityRequest;
import com.nldv.rentalroom.dto.AmenityResponse;
import com.nldv.rentalroom.pojo.Amenity;
import com.nldv.rentalroom.service.AmenityService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public ResponseEntity<List<AmenityResponse>> findAll() {

        List<AmenityResponse> result =
                amenityService.findAll()
                        .stream()
                        .map(AmenityResponse::fromEntity)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityResponse> findById(
            @PathVariable Integer id) {

        Amenity amenity = amenityService.findById(id);

        if (amenity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                AmenityResponse.fromEntity(amenity)
        );
    }

    @PostMapping
    public ResponseEntity<AmenityResponse> create(
            @Valid @RequestBody AmenityRequest request) {

        Amenity amenity = new Amenity();

        amenity.setName(request.getName());
        amenity.setDescription(request.getDescription());
        amenity.setStatus(request.getStatus());

        Amenity saved = amenityService.save(amenity);

        return ResponseEntity.ok(
                AmenityResponse.fromEntity(saved)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmenityResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody AmenityRequest request) {

        Amenity amenity = amenityService.findById(id);

        if (amenity == null) {
            return ResponseEntity.notFound().build();
        }

        amenity.setName(request.getName());
        amenity.setDescription(request.getDescription());
        amenity.setStatus(request.getStatus());

        Amenity saved = amenityService.save(amenity);

        return ResponseEntity.ok(
                AmenityResponse.fromEntity(saved)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Integer id) {

        Amenity amenity = amenityService.findById(id);

        if (amenity == null) {
            return ResponseEntity.notFound().build();
        }

        amenityService.deleteById(id);

        return ResponseEntity.ok(
                "Xóa tiện ích thành công"
        );
    }
}