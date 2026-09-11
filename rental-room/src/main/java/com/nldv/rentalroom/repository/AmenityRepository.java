package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, Integer> {
}