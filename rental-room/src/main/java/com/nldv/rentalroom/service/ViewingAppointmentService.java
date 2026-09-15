package com.nldv.rentalroom.service;

import com.nldv.rentalroom.dto.ViewingAppointmentCreateRequest;
import com.nldv.rentalroom.dto.ViewingAppointmentResponse;
import com.nldv.rentalroom.enums.RoomStatus;
import com.nldv.rentalroom.enums.ViewingStatus;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.pojo.ViewingAppointment;
import com.nldv.rentalroom.repository.RoomRepository;
import com.nldv.rentalroom.repository.UserRepository;
import com.nldv.rentalroom.repository.ViewingAppointmentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ViewingAppointmentService {

    private final ViewingAppointmentRepository repository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ViewingAppointmentService(
            ViewingAppointmentRepository repository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            NotificationService notificationService) {

        this.repository = repository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // =========================================================
    // CUSTOMER - CREATE VIEWING
    // =========================================================

    @Transactional
    public ViewingAppointmentResponse create(
            Integer customerId,
            ViewingAppointmentCreateRequest request) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy khách hàng"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy phòng"));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new IllegalArgumentException(
                    "Phòng hiện không nhận lịch xem");
        }

        if (room.getApprovalStatus() == null
                || !"APPROVED".equals(
                        room.getApprovalStatus().name())) {

            throw new IllegalArgumentException(
                    "Phòng chưa được duyệt");
        }

        boolean duplicated =
                repository
                        .existsByRoomIdAndAppointmentDateAndAppointmentTimeAndStatusIn(
                                room.getId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime(),
                                List.of(
                                        ViewingStatus.PENDING,
                                        ViewingStatus.APPROVED
                                )
                        );

        if (duplicated) {
            throw new IllegalArgumentException(
                    "Khung giờ này đã có lịch xem phòng");
        }

        ViewingAppointment appointment =
                new ViewingAppointment();

        appointment.setCustomer(customer);
        appointment.setLandlord(room.getLandlord());
        appointment.setRoom(room);
        appointment.setAppointmentDate(
                request.getAppointmentDate());
        appointment.setAppointmentTime(
                request.getAppointmentTime());
        appointment.setMessage(
                request.getMessage());
        appointment.setStatus(
                ViewingStatus.PENDING);

        ViewingAppointment saved =
                repository.save(appointment);

        notificationService.createNotification(
                room.getLandlord().getId(),
                "Có yêu cầu xem phòng mới",
                "Khách hàng "
                + customer.getUsername()
                + " muốn xem phòng "
                + room.getRoomNumber(),
                "VIEWING"
        );

        return ViewingAppointmentResponse
                .fromEntity(saved);
    }

    // =========================================================
    // CUSTOMER - LIST
    // =========================================================

    @Transactional(readOnly = true)
    public List<ViewingAppointmentResponse>
            getCustomerAppointments(Integer customerId) {

        return repository
                .findByCustomerIdOrderByAppointmentDateAscAppointmentTimeAsc(
                        customerId)
                .stream()
                .map(ViewingAppointmentResponse::fromEntity)
                .toList();
    }

    // =========================================================
    // CUSTOMER - DETAIL
    // =========================================================

    @Transactional(readOnly = true)
    public ViewingAppointmentResponse
            getCustomerAppointment(
                    Integer customerId,
                    Integer id) {

        ViewingAppointment appointment =
                find(id);

        checkCustomer(
                appointment,
                customerId);

        return ViewingAppointmentResponse
                .fromEntity(appointment);
    }

    // =========================================================
    // CUSTOMER - CANCEL
    // =========================================================

    @Transactional
    public ViewingAppointmentResponse
            cancelByCustomer(
                    Integer customerId,
                    Integer id) {

        ViewingAppointment appointment =
                find(id);

        checkCustomer(
                appointment,
                customerId);

        if (appointment.getStatus()
                != ViewingStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Chỉ có thể hủy lịch PENDING");
        }

        appointment.setStatus(
                ViewingStatus.CANCELLED);

        ViewingAppointment updated =
                repository.save(appointment);

        notifyLandlord(
                updated,
                "Lịch xem phòng đã bị hủy",
                "Khách hàng đã hủy lịch xem phòng "
                + updated.getRoom().getRoomNumber()
        );

        return ViewingAppointmentResponse
                .fromEntity(updated);
    }

    // =========================================================
    // LANDLORD - LIST
    // =========================================================

    @Transactional(readOnly = true)
    public List<ViewingAppointmentResponse>
            getLandlordAppointments(
                    Integer landlordId) {

        return repository
                .findByLandlordIdOrderByAppointmentDateAscAppointmentTimeAsc(
                        landlordId)
                .stream()
                .map(ViewingAppointmentResponse::fromEntity)
                .toList();
    }

    // =========================================================
    // LANDLORD - DETAIL
    // =========================================================

    @Transactional(readOnly = true)
    public ViewingAppointmentResponse
            getLandlordAppointment(
                    Integer landlordId,
                    Integer id) {

        ViewingAppointment appointment =
                find(id);

        checkLandlord(
                appointment,
                landlordId);

        return ViewingAppointmentResponse
                .fromEntity(appointment);
    }

    // =========================================================
    // LANDLORD - APPROVE
    // =========================================================

    @Transactional
    public ViewingAppointmentResponse
            approve(
                    Integer landlordId,
                    Integer id) {

        ViewingAppointment appointment =
                find(id);

        checkLandlord(
                appointment,
                landlordId);

        if (appointment.getStatus()
                != ViewingStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Chỉ có thể duyệt lịch PENDING");
        }

        appointment.setStatus(
                ViewingStatus.APPROVED);

        ViewingAppointment updated =
                repository.save(appointment);

        notificationService.createNotification(
                updated.getCustomer().getId(),
                "Lịch xem phòng được chấp nhận",
                "Lịch xem phòng "
                + updated.getRoom().getRoomNumber()
                + " đã được chấp nhận.",
                "VIEWING"
        );

        return ViewingAppointmentResponse
                .fromEntity(updated);
    }

    // =========================================================
    // LANDLORD - REJECT
    // =========================================================

    @Transactional
    public ViewingAppointmentResponse
            reject(
                    Integer landlordId,
                    Integer id) {

        ViewingAppointment appointment =
                find(id);

        checkLandlord(
                appointment,
                landlordId);

        if (appointment.getStatus()
                != ViewingStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Chỉ có thể từ chối lịch PENDING");
        }

        appointment.setStatus(
                ViewingStatus.REJECTED);

        ViewingAppointment updated =
                repository.save(appointment);

        notificationService.createNotification(
                updated.getCustomer().getId(),
                "Lịch xem phòng bị từ chối",
                "Lịch xem phòng "
                + updated.getRoom().getRoomNumber()
                + " đã bị từ chối.",
                "VIEWING"
        );

        return ViewingAppointmentResponse
                .fromEntity(updated);
    }

    // =========================================================
    // LANDLORD - COMPLETE
    // =========================================================

    @Transactional
    public ViewingAppointmentResponse
            complete(
                    Integer landlordId,
                    Integer id) {

        ViewingAppointment appointment =
                find(id);

        checkLandlord(
                appointment,
                landlordId);

        if (appointment.getStatus()
                != ViewingStatus.APPROVED) {

            throw new IllegalArgumentException(
                    "Chỉ có thể hoàn tất lịch APPROVED");
        }

        appointment.setStatus(
                ViewingStatus.COMPLETED);

        ViewingAppointment updated =
                repository.save(appointment);

        return ViewingAppointmentResponse
                .fromEntity(updated);
    }

    // =========================================================
    // COMMON
    // =========================================================

    @Transactional(readOnly = true)
    protected ViewingAppointment find(
            Integer id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy lịch xem phòng"));
    }

    private void checkCustomer(
            ViewingAppointment appointment,
            Integer customerId) {

        if (appointment.getCustomer() == null
                || !appointment
                        .getCustomer()
                        .getId()
                        .equals(customerId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền truy cập lịch này");
        }
    }

    private void checkLandlord(
            ViewingAppointment appointment,
            Integer landlordId) {

        if (appointment.getLandlord() == null
                || !appointment
                        .getLandlord()
                        .getId()
                        .equals(landlordId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền xử lý lịch này");
        }
    }

    private void notifyLandlord(
            ViewingAppointment appointment,
            String title,
            String content) {

        notificationService.createNotification(
                appointment.getLandlord().getId(),
                title,
                content,
                "VIEWING"
        );
    }
}