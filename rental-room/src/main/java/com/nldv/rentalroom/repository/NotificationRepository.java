package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}