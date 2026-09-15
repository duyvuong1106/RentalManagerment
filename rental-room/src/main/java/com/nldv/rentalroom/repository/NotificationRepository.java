package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserIdOrderByCreatedDateDesc(
            Integer userId);

    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedDateDesc(
            Integer userId);

    long countByUserIdAndIsReadFalse(
            Integer userId);
}
