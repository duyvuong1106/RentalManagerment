/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.dto.UserResponse;
import com.nldv.rentalroom.dto.UserStatusRequest;
import com.nldv.rentalroom.dto.UserUpdateRequest;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(
            UserService userService) {

        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {

        List<UserResponse> users =
                userService.findAll()
                        .stream()
                        .map(UserResponse::fromUser)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(
            @PathVariable Integer id) {

        User user = userService.findById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                UserResponse.fromUser(user)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody UserUpdateRequest request) {

        User user =
                userService.updateUser(id, request);

        return ResponseEntity.ok(
                UserResponse.fromUser(user)
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponse> changeStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UserStatusRequest request) {

        User user =
                userService.changeStatus(
                        id,
                        request.getStatus()
                );

        return ResponseEntity.ok(
                UserResponse.fromUser(user)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Integer id) {

        User user = userService.findById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteById(id);

        return ResponseEntity.ok(
                "Xóa người dùng thành công"
        );
    }
}