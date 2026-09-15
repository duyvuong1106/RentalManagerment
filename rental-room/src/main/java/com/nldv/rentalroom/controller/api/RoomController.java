package com.nldv.rentalroom.controller.api;

import com.nldv.rentalroom.dto.RoomResponse;
import com.nldv.rentalroom.service.RoomService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms(
            @RequestParam(required = false) Integer areaId,
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) BigDecimal minArea,
            @RequestParam(required = false) BigDecimal maxArea,
            @RequestParam(required = false) Integer amenityId) {

        boolean hasFilter = areaId != null
                || roomTypeId != null
                || minPrice != null
                || maxPrice != null
                || minArea != null
                || maxArea != null
                || amenityId != null;

        if (!hasFilter) {
            return ResponseEntity.ok(roomService.getPublicRooms());
        }

        return ResponseEntity.ok(
                roomService.searchPublicRooms(
                        areaId, roomTypeId, minPrice, maxPrice,
                        minArea, maxArea, amenityId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(roomService.getPublicRoom(id));
    }
}
