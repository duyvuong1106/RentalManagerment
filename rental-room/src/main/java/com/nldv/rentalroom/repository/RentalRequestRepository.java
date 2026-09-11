package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.RentalRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Integer> {
}