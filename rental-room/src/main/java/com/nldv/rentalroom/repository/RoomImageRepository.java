package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.RoomImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRepository extends JpaRepository<RoomImage, Integer> {

    List<RoomImage> findByRoomId(Integer roomId);

    long countByRoomId(Integer roomId);

    boolean existsByRoomIdAndIsThumbnailTrue(Integer roomId);
}
