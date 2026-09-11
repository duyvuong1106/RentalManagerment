package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}