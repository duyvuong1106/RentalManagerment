/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.service;

/**
 *
 * @author ASUS
 */


import com.nldv.rentalroom.dto.RentalRequestCreateRequest;
import com.nldv.rentalroom.dto.RentalRequestResponse;
import com.nldv.rentalroom.enums.RentalRequestStatus;
import com.nldv.rentalroom.enums.RoomStatus;
import com.nldv.rentalroom.pojo.RentalRequest;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.repository.RentalRequestRepository;
import com.nldv.rentalroom.repository.RoomRepository;
import com.nldv.rentalroom.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public RentalRequestService(
            RentalRequestRepository rentalRequestRepository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            NotificationService notificationService) {

        this.rentalRequestRepository =
                rentalRequestRepository;

        this.roomRepository =
                roomRepository;

        this.userRepository =
                userRepository;

        this.notificationService =
                notificationService;
    }

    // ============================================================
    // CUSTOMER - CREATE
    // ============================================================

    @Transactional
    public RentalRequestResponse createRequest(
            Integer customerId,
            RentalRequestCreateRequest request) {

        User customer = userRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy khách hàng"));

        Room room = roomRepository
                .findById(request.getRoomId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy phòng"));

        // Phòng phải còn trống
        if (room.getStatus() != RoomStatus.AVAILABLE) {

            throw new IllegalArgumentException(
                    "Phòng hiện tại không còn trống");
        }

        // Không cho cùng một khách gửi nhiều
        // yêu cầu PENDING cho cùng một phòng
        boolean exists =
                rentalRequestRepository
                        .existsByUserIdAndRoomIdAndStatus(
                                customerId,
                                room.getId(),
                                RentalRequestStatus.PENDING);

        if (exists) {

            throw new IllegalArgumentException(
                    "Bạn đã gửi yêu cầu thuê phòng này");
        }

        RentalRequest rentalRequest =
                new RentalRequest();

        rentalRequest.setUser(customer);
        rentalRequest.setRoom(room);
        rentalRequest.setMessage(
                request.getMessage());

        rentalRequest.setStatus(
                RentalRequestStatus.PENDING);

        rentalRequest.setRequestDate(
                LocalDateTime.now());

        rentalRequest.setProcessDate(null);

        RentalRequest saved =
                rentalRequestRepository.save(
                        rentalRequest);

        // Thông báo cho chủ trọ
        if (room.getLandlord() != null) {

            notificationService.createNotification(
                    room.getLandlord().getId(),
                    "Có yêu cầu thuê phòng mới",
                    "Khách hàng "
                    + customer.getUsername()
                    + " đã gửi yêu cầu thuê phòng "
                    + room.getRoomNumber()
                    + ".",
                    "RENTAL_REQUEST"
            );
        }

        return toResponse(saved);
    }

    // ============================================================
    // CUSTOMER - LIST
    // ============================================================

    public List<RentalRequestResponse> getCustomerRequests(
            Integer customerId) {

        return rentalRequestRepository
                .findByUserId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // CUSTOMER - DETAIL
    // ============================================================

    public RentalRequestResponse getCustomerRequest(
            Integer customerId,
            Integer requestId) {

        RentalRequest request =
                rentalRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy yêu cầu thuê"));

        checkCustomerOwnership(
                request,
                customerId);

        return toResponse(request);
    }

    // ============================================================
    // CUSTOMER - CANCEL
    // ============================================================

    @Transactional
    public RentalRequestResponse cancelRequest(
            Integer customerId,
            Integer requestId) {

        RentalRequest request =
                rentalRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy yêu cầu thuê"));

        checkCustomerOwnership(
                request,
                customerId);

        if (request.getStatus()
                != RentalRequestStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Chỉ có thể hủy yêu cầu đang chờ xử lý");
        }

        request.setStatus(
                RentalRequestStatus.CANCELLED);

        request.setProcessDate(
                LocalDateTime.now());

        RentalRequest updated =
                rentalRequestRepository.save(request);

        Room room = request.getRoom();

        // Thông báo chủ trọ
        if (room.getLandlord() != null) {

            notificationService.createNotification(
                    room.getLandlord().getId(),
                    "Yêu cầu thuê phòng đã bị hủy",
                    "Khách hàng "
                    + request.getUser().getUsername()
                    + " đã hủy yêu cầu thuê phòng "
                    + room.getRoomNumber()
                    + ".",
                    "RENTAL_REQUEST"
            );
        }

        return toResponse(updated);
    }

    // ============================================================
    // LANDLORD - LIST
    // ============================================================

    public List<RentalRequestResponse> getLandlordRequests(
            Integer landlordId) {

        return rentalRequestRepository
                .findByRoomLandlordId(landlordId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // LANDLORD - DETAIL
    // ============================================================

    public RentalRequestResponse getLandlordRequest(
            Integer landlordId,
            Integer requestId) {

        RentalRequest request =
                rentalRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy yêu cầu thuê"));

        checkLandlordOwnership(
                request,
                landlordId);

        return toResponse(request);
    }

    // ============================================================
    // LANDLORD - APPROVE
    // ============================================================

    @Transactional
    public RentalRequestResponse approveRequest(
            Integer landlordId,
            Integer requestId) {

        RentalRequest request =
                rentalRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy yêu cầu thuê"));

        checkLandlordOwnership(
                request,
                landlordId);

        // Chỉ được duyệt PENDING
        if (request.getStatus()
                != RentalRequestStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Yêu cầu thuê không ở trạng thái PENDING");
        }

        Room room = request.getRoom();

        // Phòng phải AVAILABLE
        if (room.getStatus()
                != RoomStatus.AVAILABLE) {

            throw new IllegalArgumentException(
                    "Phòng hiện không còn trống");
        }

        // ==========================================
        // DUYỆT REQUEST HIỆN TẠI
        // ==========================================

        request.setStatus(
                RentalRequestStatus.APPROVED);

        request.setProcessDate(
                LocalDateTime.now());

        // ==========================================
        // CHUYỂN PHÒNG SANG PENDING
        // ==========================================

        room.setStatus(
                RoomStatus.PENDING);

        roomRepository.save(room);

        RentalRequest saved =
                rentalRequestRepository.save(
                        request);

        // ==========================================
        // TỪ CHỐI CÁC REQUEST PENDING KHÁC
        // ==========================================

        List<RentalRequest> otherRequests =
                rentalRequestRepository
                        .findByRoomIdAndStatus(
                                room.getId(),
                                RentalRequestStatus.PENDING);

        for (RentalRequest otherRequest
                : otherRequests) {

            if (!otherRequest.getId()
                    .equals(request.getId())) {

                otherRequest.setStatus(
                        RentalRequestStatus.REJECTED);

                otherRequest.setProcessDate(
                        LocalDateTime.now());

                notificationService.createNotification(
                        otherRequest.getUser().getId(),
                        "Yêu cầu thuê phòng bị từ chối",
                        "Phòng "
                        + room.getRoomNumber()
                        + " đã được duyệt cho khách hàng khác.",
                        "RENTAL_REQUEST"
                );
            }
        }

        rentalRequestRepository.saveAll(
                otherRequests);

        // ==========================================
        // THÔNG BÁO KHÁCH ĐƯỢC DUYỆT
        // ==========================================

        notificationService.createNotification(
                request.getUser().getId(),
                "Yêu cầu thuê phòng được chấp nhận",
                "Yêu cầu thuê phòng "
                + room.getRoomNumber()
                + " của bạn đã được chủ trọ chấp nhận.",
                "RENTAL_REQUEST"
        );

        return toResponse(saved);
    }

    // ============================================================
    // LANDLORD - REJECT
    // ============================================================

    @Transactional
    public RentalRequestResponse rejectRequest(
            Integer landlordId,
            Integer requestId) {

        RentalRequest request =
                rentalRequestRepository
                        .findById(requestId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy yêu cầu thuê"));

        checkLandlordOwnership(
                request,
                landlordId);

        if (request.getStatus()
                != RentalRequestStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Chỉ có thể từ chối yêu cầu đang chờ xử lý");
        }

        request.setStatus(
                RentalRequestStatus.REJECTED);

        request.setProcessDate(
                LocalDateTime.now());

        RentalRequest updated =
                rentalRequestRepository.save(
                        request);

        notificationService.createNotification(
                request.getUser().getId(),
                "Yêu cầu thuê phòng bị từ chối",
                "Yêu cầu thuê phòng "
                + request.getRoom().getRoomNumber()
                + " của bạn đã bị chủ trọ từ chối.",
                "RENTAL_REQUEST"
        );

        return toResponse(updated);
    }

    // ============================================================
    // SECURITY CHECK
    // ============================================================

    private void checkCustomerOwnership(
            RentalRequest request,
            Integer customerId) {

        if (request.getUser() == null
                || request.getUser().getId() == null
                || !request.getUser()
                        .getId()
                        .equals(customerId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền truy cập yêu cầu này");
        }
    }

    private void checkLandlordOwnership(
            RentalRequest request,
            Integer landlordId) {

        if (request.getRoom() == null
                || request.getRoom().getLandlord() == null) {

            throw new IllegalArgumentException(
                    "Phòng chưa có chủ trọ");
        }

        if (!request.getRoom()
                .getLandlord()
                .getId()
                .equals(landlordId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền xử lý yêu cầu này");
        }
    }

    // ============================================================
    // RESPONSE
    // ============================================================

    private RentalRequestResponse toResponse(
            RentalRequest request) {

        RentalRequestResponse response =
                new RentalRequestResponse();

        response.setId(
                request.getId());

        response.setRoomId(
                request.getRoom().getId());

        response.setRoomNumber(
                request.getRoom().getRoomNumber());

        response.setCustomerId(
                request.getUser().getId());

        response.setCustomerUsername(
                request.getUser().getUsername());

        response.setMessage(
                request.getMessage());

        response.setStatus(
                request.getStatus());

        response.setRequestDate(
                request.getRequestDate());

        response.setProcessDate(
                request.getProcessDate());

        return response;
    }
}
