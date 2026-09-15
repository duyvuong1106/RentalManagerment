package com.nldv.rentalroom.controller.api;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.dto.NotificationResponse;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.NotificationService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

   
    @GetMapping
    public ResponseEntity<List<NotificationResponse>>
            getNotifications(
                    Authentication authentication) {

        Integer userId =
                getUserId(authentication);

        return ResponseEntity.ok(
                notificationService
                        .getUserNotifications(userId));
    }

    
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>>
            getUnreadNotifications(
                    Authentication authentication) {

        Integer userId =
                getUserId(authentication);

        return ResponseEntity.ok(
                notificationService
                        .getUnreadNotifications(userId));
    }

   
    @GetMapping("/unread/count")
    public ResponseEntity<Long>
            countUnreadNotifications(
                    Authentication authentication) {

        Integer userId =
                getUserId(authentication);

        return ResponseEntity.ok(
                notificationService
                        .countUnreadNotifications(userId));
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse>
            getNotification(
                    @PathVariable Integer id,
                    Authentication authentication) {

        Integer userId =
                getUserId(authentication);

        return ResponseEntity.ok(
                notificationService
                        .getNotification(
                                userId,
                                id));
    }

    
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse>
            markAsRead(
                    @PathVariable Integer id,
                    Authentication authentication) {

        Integer userId =
                getUserId(authentication);

        return ResponseEntity.ok(
                notificationService
                        .markAsRead(
                                userId,
                                id));
    }

     
    @PutMapping("/read-all")
    public ResponseEntity<Void>
            markAllAsRead(
                    Authentication authentication) {

        Integer userId =
                getUserId(authentication);

        notificationService
                .markAllAsRead(userId);

        return ResponseEntity.noContent()
                .build();
    }

    private Integer getUserId(
            Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        authentication.getPrincipal();

        return userDetails
                .getUser()
                .getId();
    }
}
