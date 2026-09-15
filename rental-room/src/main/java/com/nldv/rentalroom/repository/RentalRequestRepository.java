package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.enums.RentalRequestStatus;
import com.nldv.rentalroom.pojo.RentalRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRequestRepository
        extends JpaRepository<RentalRequest, Integer> {

    List<RentalRequest> findByUserId(Integer userId);

    List<RentalRequest> findByRoomId(Integer roomId);

    List<RentalRequest> findByRoomLandlordId(
            Integer landlordId);

    List<RentalRequest> findByRoomIdAndStatus(
            Integer roomId,
            RentalRequestStatus status);

    boolean existsByUserIdAndRoomIdAndStatus(
            Integer userId,
            Integer roomId,
            RentalRequestStatus status);
}
