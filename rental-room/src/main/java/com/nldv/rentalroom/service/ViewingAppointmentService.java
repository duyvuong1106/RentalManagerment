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

    public ViewingAppointmentService(ViewingAppointmentRepository repository, RoomRepository roomRepository,
                                     UserRepository userRepository, NotificationService notificationService) {
        this.repository = repository; this.roomRepository = roomRepository; this.userRepository = userRepository; this.notificationService = notificationService;
    }

    @Transactional
    public ViewingAppointmentResponse create(Integer customerId, ViewingAppointmentCreateRequest request) {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng"));
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng"));
        if (room.getStatus() != RoomStatus.AVAILABLE || room.getApprovalStatus() == null || room.getApprovalStatus().name().equals("APPROVED") == false) {
            throw new IllegalArgumentException("Phòng hiện không nhận lịch xem");
        }
        if (repository.existsByRoomIdAndAppointmentDateAndAppointmentTimeAndStatusIn(
                room.getId(), request.getAppointmentDate(), request.getAppointmentTime(),
                List.of(ViewingStatus.PENDING, ViewingStatus.APPROVED))) {
            throw new IllegalArgumentException("Khung giờ này đã có lịch xem phòng");
        }
        ViewingAppointment a = new ViewingAppointment();
        a.setCustomer(customer); a.setLandlord(room.getLandlord()); a.setRoom(room);
        a.setAppointmentDate(request.getAppointmentDate()); a.setAppointmentTime(request.getAppointmentTime());
        a.setMessage(request.getMessage()); a.setStatus(ViewingStatus.PENDING);
        ViewingAppointment saved = repository.save(a);
        notificationService.createNotification(room.getLandlord().getId(), "Có yêu cầu xem phòng mới",
                "Khách hàng " + customer.getUsername() + " muốn xem phòng " + room.getRoomNumber(), "VIEWING");
        return ViewingAppointmentResponse.fromEntity(saved);
    }

    public List<ViewingAppointmentResponse> getCustomerAppointments(Integer customerId) {
        return repository.findByCustomerIdOrderByAppointmentDateAscAppointmentTimeAsc(customerId).stream().map(ViewingAppointmentResponse::fromEntity).toList();
    }

    public ViewingAppointmentResponse getCustomerAppointment(Integer customerId, Integer id) {
        ViewingAppointment a = find(id); checkCustomer(a, customerId); return ViewingAppointmentResponse.fromEntity(a);
    }

    public List<ViewingAppointmentResponse> getLandlordAppointments(Integer landlordId) {
        return repository.findByLandlordIdOrderByAppointmentDateAscAppointmentTimeAsc(landlordId).stream().map(ViewingAppointmentResponse::fromEntity).toList();
    }

    public ViewingAppointmentResponse getLandlordAppointment(Integer landlordId, Integer id) {
        ViewingAppointment a = find(id); checkLandlord(a, landlordId); return ViewingAppointmentResponse.fromEntity(a);
    }

    @Transactional
    public ViewingAppointmentResponse cancelByCustomer(Integer customerId, Integer id) {
        ViewingAppointment a = find(id); checkCustomer(a, customerId);
        if (a.getStatus() != ViewingStatus.PENDING) throw new IllegalArgumentException("Chỉ có thể hủy lịch PENDING");
        a.setStatus(ViewingStatus.CANCELLED); repository.save(a);
        notifyLandlord(a, "Lịch xem phòng đã bị hủy", "Khách hàng đã hủy lịch xem phòng " + a.getRoom().getRoomNumber());
        return ViewingAppointmentResponse.fromEntity(a);
    }

    @Transactional
    public ViewingAppointmentResponse approve(Integer landlordId, Integer id) {
        ViewingAppointment a = find(id); checkLandlord(a, landlordId);
        if (a.getStatus() != ViewingStatus.PENDING) throw new IllegalArgumentException("Chỉ có thể duyệt lịch PENDING");
        a.setStatus(ViewingStatus.APPROVED); repository.save(a);
        notificationService.createNotification(a.getCustomer().getId(), "Lịch xem phòng được chấp nhận", "Lịch xem phòng " + a.getRoom().getRoomNumber() + " đã được chấp nhận.", "VIEWING");
        return ViewingAppointmentResponse.fromEntity(a);
    }

    @Transactional
    public ViewingAppointmentResponse reject(Integer landlordId, Integer id) {
        ViewingAppointment a = find(id); checkLandlord(a, landlordId);
        if (a.getStatus() != ViewingStatus.PENDING) throw new IllegalArgumentException("Chỉ có thể từ chối lịch PENDING");
        a.setStatus(ViewingStatus.REJECTED); repository.save(a);
        notificationService.createNotification(a.getCustomer().getId(), "Lịch xem phòng bị từ chối", "Lịch xem phòng " + a.getRoom().getRoomNumber() + " đã bị từ chối.", "VIEWING");
        return ViewingAppointmentResponse.fromEntity(a);
    }

    @Transactional
    public ViewingAppointmentResponse complete(Integer landlordId, Integer id) {
        ViewingAppointment a = find(id); checkLandlord(a, landlordId);
        if (a.getStatus() != ViewingStatus.APPROVED) throw new IllegalArgumentException("Chỉ có thể hoàn tất lịch APPROVED");
        a.setStatus(ViewingStatus.COMPLETED); repository.save(a); return ViewingAppointmentResponse.fromEntity(a);
    }

    private ViewingAppointment find(Integer id) { return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch xem phòng")); }
    private void checkCustomer(ViewingAppointment a, Integer customerId) { if (a.getCustomer() == null || !a.getCustomer().getId().equals(customerId)) throw new IllegalArgumentException("Bạn không có quyền truy cập lịch này"); }
    private void checkLandlord(ViewingAppointment a, Integer landlordId) { if (a.getLandlord() == null || !a.getLandlord().getId().equals(landlordId)) throw new IllegalArgumentException("Bạn không có quyền xử lý lịch này"); }
    private void notifyLandlord(ViewingAppointment a, String title, String content) { notificationService.createNotification(a.getLandlord().getId(), title, content, "VIEWING"); }
}
