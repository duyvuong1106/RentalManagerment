package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}