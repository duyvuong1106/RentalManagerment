/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.service;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.NotificationCreateRequest;
import com.nldv.rentalroom.dto.NotificationResponse;
import com.nldv.rentalroom.pojo.Notification;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.repository.NotificationRepository;
import com.nldv.rentalroom.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public NotificationResponse createNotification(
            Integer userId,
            String title,
            String content,
            String type) {

        NotificationCreateRequest request = new NotificationCreateRequest();

        request.setUserId(userId);
        request.setTitle(title);
        request.setContent(content);
        request.setType(type);

        return createNotification(request);
    }

    @Transactional
    public NotificationResponse createNotification(
            NotificationCreateRequest request) {

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy người dùng"));

        Notification notification
                = new Notification();

        notification.setUser(user);
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setType(request.getType());
        notification.setIsRead(false);

        Notification saved
                = notificationRepository.save(notification);

        return toResponse(saved);
    }

    public List<NotificationResponse> getUserNotifications(
            Integer userId) {

        return notificationRepository
                .findByUserIdOrderByCreatedDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationResponse> getUnreadNotifications(
            Integer userId) {

        return notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedDateDesc(
                        userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public long countUnreadNotifications(
            Integer userId) {

        return notificationRepository
                .countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public NotificationResponse markAsRead(
            Integer userId,
            Integer notificationId) {

        Notification notification
                = notificationRepository
                        .findById(notificationId)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "Không tìm thấy thông báo"));

        checkOwnership(userId, notification);

        notification.setIsRead(true);

        return toResponse(
                notificationRepository.save(notification));
    }

    @Transactional
    public void markAllAsRead(
            Integer userId) {

        List<Notification> notifications
                = notificationRepository
                        .findByUserIdAndIsReadFalseOrderByCreatedDateDesc(
                                userId);

        for (Notification notification : notifications) {
            notification.setIsRead(true);
        }

        notificationRepository.saveAll(notifications);
    }

    public NotificationResponse getNotification(
            Integer userId,
            Integer notificationId) {

        Notification notification
                = notificationRepository
                        .findById(notificationId)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "Không tìm thấy thông báo"));

        checkOwnership(userId, notification);

        return toResponse(notification);
    }

    private void checkOwnership(
            Integer userId,
            Notification notification) {

        if (!notification.getUser()
                .getId()
                .equals(userId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền truy cập thông báo này");
        }
    }

    private NotificationResponse toResponse(
            Notification notification) {

        NotificationResponse response
                = new NotificationResponse();

        response.setId(notification.getId());

        response.setUserId(
                notification.getUser().getId());

        response.setTitle(
                notification.getTitle());

        response.setContent(
                notification.getContent());

        response.setType(
                notification.getType());

        response.setIsRead(
                notification.getIsRead());

        response.setCreatedDate(
                notification.getCreatedDate());

        response.setUpdatedDate(
                notification.getUpdatedDate());

        return response;
    }
}
