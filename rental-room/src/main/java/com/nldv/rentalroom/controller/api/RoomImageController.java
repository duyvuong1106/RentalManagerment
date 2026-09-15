/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.RoomImageResponse;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.pojo.RoomImage;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.service.RoomImageService;
import com.nldv.rentalroom.service.RoomService;
import com.nldv.rentalroom.service.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class RoomImageController {

    private final RoomImageService roomImageService;
    private final RoomService roomService;
    private final UserService userService;

    public RoomImageController(
            RoomImageService roomImageService,
            RoomService roomService,
            UserService userService) {

        this.roomImageService = roomImageService;
        this.roomService = roomService;
        this.userService = userService;
    }

    // =====================================
    // PUBLIC
    // =====================================
    @GetMapping("/rooms/{roomId}/images")
    public ResponseEntity<?> getRoomImages(
            @PathVariable Integer roomId) {

        Room room = roomService.findById(roomId);

        if (room == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy phòng");
        }

        List<RoomImageResponse> images
                = roomImageService
                        .findByRoomId(roomId)
                        .stream()
                        .map(RoomImageResponse::fromEntity)
                        .toList();

        return ResponseEntity.ok(images);
    }

    // =====================================
    // LANDLORD - UPLOAD
    // =====================================
    @PostMapping(
            value = "/landlord/rooms/{roomId}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadImage(
            @PathVariable Integer roomId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(
                    value = "thumbnail",
                    defaultValue = "false"
            ) boolean thumbnail,
            Authentication authentication) {

        User landlord
                = getCurrentUser(authentication);

        if (landlord == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Không tìm thấy tài khoản");
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

            RoomImage image
                    = roomImageService.uploadImage(
                            room,
                            file,
                            thumbnail
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            RoomImageResponse
                                    .fromEntity(image)
                    );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (IOException e) {

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            "Không thể upload hình ảnh"
                    );
        }
    }

    
    @DeleteMapping(
            "/landlord/rooms/{roomId}/images/{imageId}"
    )
    public ResponseEntity<?> deleteImage(
            @PathVariable Integer roomId,
            @PathVariable Integer imageId,
            Authentication authentication) {

        User landlord
                = getCurrentUser(authentication);

        if (landlord == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Không tìm thấy tài khoản");
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

        RoomImage image
                = roomImageService.findById(imageId);

        if (image == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy hình ảnh");
        }

        if (image.getRoom() == null
                || !image.getRoom()
                        .getId()
                        .equals(roomId)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            "Hình ảnh không thuộc phòng này"
                    );
        }

        try {

            roomImageService.deleteImage(image);

            return ResponseEntity.ok(
                    "Xóa hình ảnh thành công"
            );

        } catch (IOException e) {

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            "Không thể xóa hình ảnh"
                    );
        }
    }

    
    @PutMapping(
            "/landlord/rooms/{roomId}/images/{imageId}/thumbnail"
    )
    public ResponseEntity<?> setThumbnail(
            @PathVariable Integer roomId,
            @PathVariable Integer imageId,
            Authentication authentication) {

        User landlord
                = getCurrentUser(authentication);

        if (landlord == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Không tìm thấy tài khoản");
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

        RoomImage image
                = roomImageService.findById(imageId);

        if (image == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy hình ảnh");
        }

        if (image.getRoom() == null
                || !image.getRoom()
                        .getId()
                        .equals(roomId)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            "Hình ảnh không thuộc phòng này"
                    );
        }

        RoomImage updated
                = roomImageService.setThumbnail(image);

        return ResponseEntity.ok(
                RoomImageResponse.fromEntity(
                        updated)
        );
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
