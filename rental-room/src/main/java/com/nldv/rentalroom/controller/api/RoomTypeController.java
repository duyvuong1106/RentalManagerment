/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.dto.RoomTypeRequest;
import com.nldv.rentalroom.dto.RoomTypeResponse;
import com.nldv.rentalroom.pojo.RoomType;
import com.nldv.rentalroom.service.RoomTypeService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeResponse>> findAll() {

        List<RoomTypeResponse> result =
                roomTypeService.findAll()
                        .stream()
                        .map(RoomTypeResponse::fromEntity)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> findById(
            @PathVariable Integer id) {

        RoomType roomType =
                roomTypeService.findById(id);

        if (roomType == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                RoomTypeResponse.fromEntity(roomType)
        );
    }

    @PostMapping
    public ResponseEntity<RoomTypeResponse> create(
            @Valid @RequestBody RoomTypeRequest request) {

        RoomType roomType = new RoomType();

        roomType.setName(request.getName());
        roomType.setDescription(request.getDescription());
        roomType.setStatus(request.getStatus());

        RoomType saved =
                roomTypeService.save(roomType);

        return ResponseEntity.ok(
                RoomTypeResponse.fromEntity(saved)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody RoomTypeRequest request) {

        RoomType roomType =
                roomTypeService.findById(id);

        if (roomType == null) {
            return ResponseEntity.notFound().build();
        }

        roomType.setName(request.getName());
        roomType.setDescription(request.getDescription());
        roomType.setStatus(request.getStatus());

        RoomType saved =
                roomTypeService.save(roomType);

        return ResponseEntity.ok(
                RoomTypeResponse.fromEntity(saved)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Integer id) {

        RoomType roomType =
                roomTypeService.findById(id);

        if (roomType == null) {
            return ResponseEntity.notFound().build();
        }

        roomTypeService.deleteById(id);

        return ResponseEntity.ok(
                "Xóa loại phòng thành công"
        );
    }
}