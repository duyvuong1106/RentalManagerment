package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByRoomIdAndIsVisibleTrue(Integer roomId);

    List<Review> findByUserId(Integer userId);

    boolean existsByUserIdAndRoomId(Integer userId, Integer roomId);
}
