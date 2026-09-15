package com.nldv.rentalroom.controller.api;

import com.nldv.rentalroom.dto.RoomResponse;
import com.nldv.rentalroom.service.RoomService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/rooms")
public class AdminRoomController {

    private final RoomService roomService;

    public AdminRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<RoomResponse>> getPendingRooms() {
        return ResponseEntity.ok(roomService.getPendingApprovalRooms());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<RoomResponse> approveRoom(
            @PathVariable Integer id) {
        return ResponseEntity.ok(roomService.approveRoom(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<RoomResponse> rejectRoom(
            @PathVariable Integer id) {
        return ResponseEntity.ok(roomService.rejectRoom(id));
    }
}
