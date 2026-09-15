package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.RoomAmenity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomAmenityRepository
        extends JpaRepository<RoomAmenity, RoomAmenity.RoomAmenityId> {
    
    List<RoomAmenity> findByRoomId(Integer roomId);

    boolean existsByRoomIdAndAmenityId(
            Integer roomId,
            Integer amenityId);

    void deleteByRoomIdAndAmenityId(
            Integer roomId,
            Integer amenityId);

    void deleteByRoomId(Integer roomId);
}