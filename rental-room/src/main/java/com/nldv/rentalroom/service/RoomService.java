package com.nldv.rentalroom.service;

import com.nldv.rentalroom.dto.RoomCreateRequest;
import com.nldv.rentalroom.dto.RoomResponse;
import com.nldv.rentalroom.dto.RoomUpdateRequest;
import com.nldv.rentalroom.enums.RoomStatus;
import com.nldv.rentalroom.enums.RoomApprovalStatus;
import com.nldv.rentalroom.pojo.Area;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.pojo.RoomType;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.repository.AreaRepository;
import com.nldv.rentalroom.repository.RoomRepository;
import com.nldv.rentalroom.repository.RoomTypeRepository;
import com.nldv.rentalroom.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final AreaRepository areaRepository;
    private final RoomTypeRepository roomTypeRepository;

    public RoomService(
            RoomRepository roomRepository,
            UserRepository userRepository,
            AreaRepository areaRepository,
            RoomTypeRepository roomTypeRepository) {

        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.areaRepository = areaRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    // =========================
    // ADMIN / COMMON
    // =========================
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room findById(Integer id) {
        return roomRepository.findById(id).orElse(null);
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void deleteById(Integer id) {
        roomRepository.deleteById(id);
    }

    // =========================
    // LANDLORD
    // =========================
    public List<Room> findByLandlordId(Integer landlordId) {
        return roomRepository.findByLandlordId(landlordId);
    }

    public boolean existsByRoomNumber(String roomNumber) {
        return roomRepository.existsByRoomNumber(roomNumber);
    }

    @Transactional
    public RoomResponse createRoom(
            Integer landlordId,
            RoomCreateRequest request) {

        User landlord = userRepository.findById(landlordId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy chủ trọ"));

        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Khu vực không tồn tại"));

        RoomType roomType = roomTypeRepository.findById(
                request.getRoomTypeId()
        ).orElseThrow(()
                -> new IllegalArgumentException(
                        "Loại phòng không tồn tại"));

        if (roomRepository.existsByRoomNumber(
                request.getRoomNumber())) {

            throw new IllegalArgumentException(
                    "Mã phòng đã tồn tại");
        }

        Room room = new Room();

        room.setRoomNumber(request.getRoomNumber());
        room.setTitle(request.getTitle());
        room.setDescription(request.getDescription());
        room.setAddress(request.getAddress());
        room.setArea(area);
        room.setRoomType(roomType);
        room.setLandlord(landlord);
        room.setPrice(request.getPrice());

        room.setAreaSize(
                java.math.BigDecimal.valueOf(
                        request.getArea()
                )
        );

        room.setStatus(RoomStatus.AVAILABLE);
        room.setApprovalStatus(RoomApprovalStatus.PENDING);

        Room savedRoom = roomRepository.save(room);

        return RoomResponse.fromEntity(savedRoom);
    }

    public List<RoomResponse> getLandlordRooms(
            Integer landlordId) {

        return roomRepository
                .findByLandlordId(landlordId)
                .stream()
                .map(RoomResponse::fromEntity)
                .toList();
    }

    public RoomResponse getLandlordRoom(
            Integer landlordId,
            Integer roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy phòng"));

        checkOwnership(room, landlordId);

        return RoomResponse.fromEntity(room);
    }

    @Transactional
    public RoomResponse updateRoom(
            Integer landlordId,
            Integer roomId,
            RoomUpdateRequest request) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy phòng"));

        checkOwnership(room, landlordId);

        if (request.getTitle() != null) {
            room.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            room.setDescription(request.getDescription());
        }

        if (request.getAddress() != null) {
            room.setAddress(request.getAddress());
        }

        if (room.getStatus() == RoomStatus.RENTED) {
            throw new IllegalArgumentException(
                    "Không thể cập nhật phòng đang được thuê");
        }

        if (request.getRoomNumber() != null
                && !request.getRoomNumber()
                        .equals(room.getRoomNumber())) {

            if (roomRepository.existsByRoomNumber(
                    request.getRoomNumber())) {

                throw new IllegalArgumentException(
                        "Mã phòng đã tồn tại");
            }

            room.setRoomNumber(
                    request.getRoomNumber());
        }

        if (request.getAreaId() != null) {

            Area area = areaRepository.findById(
                    request.getAreaId()
            ).orElseThrow(()
                    -> new IllegalArgumentException(
                            "Khu vực không tồn tại"));

            room.setArea(area);
        }

        if (request.getRoomTypeId() != null) {

            RoomType roomType
                    = roomTypeRepository.findById(
                            request.getRoomTypeId()
                    ).orElseThrow(()
                            -> new IllegalArgumentException(
                                    "Loại phòng không tồn tại"));

            room.setRoomType(roomType);
        }

        if (request.getArea() != null) {

            room.setAreaSize(
                    java.math.BigDecimal.valueOf(
                            request.getArea()
                    )
            );
        }

        if (request.getPrice() != null) {
            room.setPrice(request.getPrice());
        }

        Room updatedRoom
                = roomRepository.save(room);

        return RoomResponse.fromEntity(
                updatedRoom);
    }

    @Transactional
    public void deleteRoom(
            Integer landlordId,
            Integer roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy phòng"));

        checkOwnership(room, landlordId);

        if (room.getStatus() == RoomStatus.RENTED) {
            throw new IllegalArgumentException(
                    "Không thể xóa phòng đang được thuê");
        }

        roomRepository.delete(room);
    }

    // =========================
    // PUBLIC
    // =========================
    public List<RoomResponse> getPublicRooms() {

        return roomRepository
                .searchAvailableRooms(
                        RoomStatus.AVAILABLE,
                        RoomApprovalStatus.APPROVED,
                        null, null, null, null, null, null, null)
                .stream()
                .map(RoomResponse::fromEntity)
                .toList();
    }

    public RoomResponse getPublicRoom(
            Integer roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy phòng"));

        return RoomResponse.fromEntity(room);
    }

    // =========================
    // PUBLIC SEARCH
    // =========================
    public List<RoomResponse> searchPublicRooms(
            Integer areaId,
            Integer roomTypeId,
            Integer minPrice,
            Integer maxPrice,
            java.math.BigDecimal minArea,
            java.math.BigDecimal maxArea,
            Integer amenityId) {

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Giá tối thiểu không được lớn hơn giá tối đa");
        }
        if (minArea != null && maxArea != null && minArea.compareTo(maxArea) > 0) {
            throw new IllegalArgumentException("Diện tích tối thiểu không được lớn hơn diện tích tối đa");
        }
        if (minPrice != null && minPrice < 0 || maxPrice != null && maxPrice < 0) {
            throw new IllegalArgumentException("Giá không được âm");
        }

        return roomRepository.searchAvailableRooms(
                RoomStatus.AVAILABLE,
                RoomApprovalStatus.APPROVED,
                areaId, roomTypeId, minPrice, maxPrice, minArea, maxArea, amenityId)
                .stream()
                .map(RoomResponse::fromEntity)
                .toList();
    }

    // =========================
    // ADMIN APPROVAL
    // =========================
    public List<RoomResponse> getPendingApprovalRooms() {
        return roomRepository.findByApprovalStatus(RoomApprovalStatus.PENDING)
                .stream()
                .map(RoomResponse::fromEntity)
                .toList();
    }

    @Transactional
    public RoomResponse approveRoom(Integer roomId) {
        Room room = findById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Không tìm thấy phòng");
        }
        if (room.getApprovalStatus() == RoomApprovalStatus.APPROVED) {
            throw new IllegalArgumentException("Phòng đã được duyệt");
        }
        room.setApprovalStatus(RoomApprovalStatus.APPROVED);
        if (room.getStatus() == null) {
            room.setStatus(RoomStatus.AVAILABLE);
        }
        return RoomResponse.fromEntity(roomRepository.save(room));
    }

    @Transactional
    public RoomResponse rejectRoom(Integer roomId) {
        Room room = findById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Không tìm thấy phòng");
        }
        if (room.getApprovalStatus() == RoomApprovalStatus.REJECTED) {
            throw new IllegalArgumentException("Phòng đã bị từ chối");
        }
        room.setApprovalStatus(RoomApprovalStatus.REJECTED);
        return RoomResponse.fromEntity(roomRepository.save(room));
    }

    // =========================
    // PRIVATE
    // =========================
    private void checkOwnership(
            Room room,
            Integer landlordId) {

        if (room.getLandlord() == null
                || room.getLandlord().getId() == null
                || !room.getLandlord()
                        .getId()
                        .equals(landlordId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền quản lý phòng này");
        }
    }
}
