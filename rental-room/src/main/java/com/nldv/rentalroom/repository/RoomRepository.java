package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.enums.RoomStatus;
import com.nldv.rentalroom.pojo.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByLandlordId(Integer landlordId);

    List<Room> findByStatus(RoomStatus status);

    List<Room> findByAreaId(Integer areaId);

    List<Room> findByRoomTypeId(Integer roomTypeId);

    Optional<Room> findByRoomNumber(String roomNumber);

    boolean existsByRoomNumber(String roomNumber);
}