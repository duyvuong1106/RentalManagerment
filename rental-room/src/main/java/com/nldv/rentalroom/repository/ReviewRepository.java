package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByRoomIdAndIsVisibleTrue(Integer roomId);

    List<Review> findByUserId(Integer userId);

    boolean existsByUserIdAndRoomId(Integer userId, Integer roomId);

    @org.springframework.data.jpa.repository.Query("SELECT AVG(r.rating) FROM Review r WHERE r.isVisible = true")
    Double averageVisibleRating();

    @org.springframework.data.jpa.repository.Query("SELECT AVG(r.rating) FROM Review r WHERE r.room.landlord.id = :landlordId AND r.isVisible = true")
    Double averageVisibleRatingByLandlordId(@org.springframework.data.repository.query.Param("landlordId") Integer landlordId);

    long countByIsVisibleTrue();
}
