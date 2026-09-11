package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
}