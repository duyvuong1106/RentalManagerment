/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.RoomCreateRequest;
import com.nldv.rentalroom.dto.RoomResponse;
import com.nldv.rentalroom.dto.RoomUpdateRequest;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.RoomService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/landlord/rooms")
public class LandlordRoomController {

    private final RoomService roomService;

    public LandlordRoomController(
            RoomService roomService) {

        this.roomService = roomService;
    }

    
    @PostMapping
    public ResponseEntity<?> createRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody RoomCreateRequest request) {

        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        try {

            Integer landlordId
                    = userDetails.getUser().getId();

            RoomResponse response
                    = roomService.createRoom(
                            landlordId,
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
    public ResponseEntity<?> getRooms(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        Integer landlordId
                = userDetails.getUser().getId();

        return ResponseEntity.ok(
                roomService.getLandlordRooms(
                        landlordId
                )
        );
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer id) {

        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        try {

            Integer landlordId
                    = userDetails.getUser().getId();

            return ResponseEntity.ok(
                    roomService.getLandlordRoom(
                            landlordId,
                            id
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer id,
            @Valid @RequestBody RoomUpdateRequest request) {

        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        try {

            Integer landlordId
                    = userDetails.getUser().getId();

            return ResponseEntity.ok(
                    roomService.updateRoom(
                            landlordId,
                            id,
                            request
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer id) {

        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        try {

            Integer landlordId
                    = userDetails.getUser().getId();

            roomService.deleteRoom(
                    landlordId,
                    id
            );

            return ResponseEntity.ok(
                    "Xóa phòng thành công"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
