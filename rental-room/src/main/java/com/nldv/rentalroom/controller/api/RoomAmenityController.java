/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.RoomAmenityResponse;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.pojo.RoomAmenity;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.service.RoomAmenityService;
import com.nldv.rentalroom.service.RoomService;
import com.nldv.rentalroom.service.UserService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoomAmenityController {

    private final RoomAmenityService roomAmenityService;
    private final RoomService roomService;
    private final UserService userService;

    public RoomAmenityController(
            RoomAmenityService roomAmenityService,
            RoomService roomService,
            UserService userService) {

        this.roomAmenityService = roomAmenityService;
        this.roomService = roomService;
        this.userService = userService;
    }

    @GetMapping("/rooms/{roomId}/amenities")
    public ResponseEntity<?> getRoomAmenities(
            @PathVariable Integer roomId) {

        Room room = roomService.findById(roomId);

        if (room == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy phòng");
        }

        List<RoomAmenityResponse> response
                = roomAmenityService
                        .findByRoomId(roomId)
                        .stream()
                        .map(RoomAmenityResponse::fromEntity)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            "/landlord/rooms/{roomId}/amenities/{amenityId}"
    )
    public ResponseEntity<?> addAmenity(
            @PathVariable Integer roomId,
            @PathVariable Integer amenityId,
            Authentication authentication) {

        User landlord
                = getCurrentUser(authentication);

        if (landlord == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        Room room
                = roomService.findById(roomId);

        if (room == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy phòng");
        }

        if (!isOwner(room, landlord)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            "Bạn không có quyền quản lý phòng này"
                    );
        }

        try {

            RoomAmenity roomAmenity
                    = roomAmenityService.addAmenity(
                            roomId,
                            amenityId
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            RoomAmenityResponse
                                    .fromEntity(roomAmenity)
                    );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @DeleteMapping(
            "/landlord/rooms/{roomId}/amenities/{amenityId}"
    )
    public ResponseEntity<?> removeAmenity(
            @PathVariable Integer roomId,
            @PathVariable Integer amenityId,
            Authentication authentication) {

        User landlord
                = getCurrentUser(authentication);

        if (landlord == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn chưa đăng nhập");
        }

        Room room
                = roomService.findById(roomId);

        if (room == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy phòng");
        }

        if (!isOwner(room, landlord)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            "Bạn không có quyền quản lý phòng này"
                    );
        }

        try {

            roomAmenityService.removeAmenity(
                    roomId,
                    amenityId
            );

            return ResponseEntity.ok(
                    "Xóa tiện ích khỏi phòng thành công"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    private User getCurrentUser(
            Authentication authentication) {

        if (authentication == null) {
            return null;
        }

        return userService.findByUsername(
                authentication.getName()
        );
    }

    private boolean isOwner(
            Room room,
            User landlord) {

        return room.getLandlord() != null
                && room.getLandlord()
                        .getId()
                        .equals(landlord.getId());
    }
}
