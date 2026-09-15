package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.enums.RoomApprovalStatus;
import com.nldv.rentalroom.enums.RoomStatus;
import com.nldv.rentalroom.pojo.Room;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByLandlordId(Integer landlordId);

    List<Room> findByStatus(RoomStatus status);

    List<Room> findByAreaId(Integer areaId);

    List<Room> findByRoomTypeId(Integer roomTypeId);

    List<Room> findByApprovalStatus(RoomApprovalStatus approvalStatus);

    Optional<Room> findByRoomNumber(String roomNumber);

    boolean existsByRoomNumber(String roomNumber);

    long countByStatus(RoomStatus status);

    long countByLandlordId(Integer landlordId);

    long countByLandlordIdAndStatus(Integer landlordId, RoomStatus status);

    @Query("""
        SELECT DISTINCT r
        FROM Room r
        LEFT JOIN r.roomAmenities ra
        WHERE r.status = :status
          AND r.approvalStatus = :approvalStatus
          AND (:areaId IS NULL OR r.area.id = :areaId)
          AND (:roomTypeId IS NULL OR r.roomType.id = :roomTypeId)
          AND (:minPrice IS NULL OR r.price >= :minPrice)
          AND (:maxPrice IS NULL OR r.price <= :maxPrice)
          AND (:minArea IS NULL OR r.areaSize >= :minArea)
          AND (:maxArea IS NULL OR r.areaSize <= :maxArea)
          AND (:amenityId IS NULL OR ra.amenity.id = :amenityId)
        ORDER BY r.createdDate DESC
    """)
    List<Room> searchAvailableRooms(
            @Param("status") RoomStatus status,
            @Param("approvalStatus") RoomApprovalStatus approvalStatus,
            @Param("areaId") Integer areaId,
            @Param("roomTypeId") Integer roomTypeId,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("minArea") BigDecimal minArea,
            @Param("maxArea") BigDecimal maxArea,
            @Param("amenityId") Integer amenityId
    );
}
