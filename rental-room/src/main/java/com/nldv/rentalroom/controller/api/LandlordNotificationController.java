package com.nldv.rentalroom.controller.api;

import com.nldv.rentalroom.dto.LandlordNotificationRequest;
import com.nldv.rentalroom.dto.NotificationResponse;
import com.nldv.rentalroom.enums.UserRole;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.service.NotificationService;
import com.nldv.rentalroom.repository.ContractRepository;
import com.nldv.rentalroom.enums.ContractStatus;
import com.nldv.rentalroom.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/landlord/notifications")
public class LandlordNotificationController {
    private final NotificationService notificationService;
    private final UserService userService;
    private final ContractRepository contractRepository;
    public LandlordNotificationController(NotificationService notificationService, UserService userService, ContractRepository contractRepository){this.notificationService=notificationService;this.userService=userService;this.contractRepository=contractRepository;}
    @PostMapping
    public ResponseEntity<NotificationResponse> send(@AuthenticationPrincipal CustomUserDetails u,
            @Valid @RequestBody LandlordNotificationRequest request){
        User customer=userService.findById(request.getCustomerId());
        if(customer==null) throw new IllegalArgumentException("Không tìm thấy khách hàng");
        if(customer.getRole()!=UserRole.CUSTOMER) throw new IllegalArgumentException("Người nhận không phải CUSTOMER");
        if(!contractRepository.existsByUserIdAndRoomLandlordIdAndStatus(request.getCustomerId(), u.getUser().getId(), ContractStatus.ACTIVE)) {
            throw new IllegalArgumentException("Bạn chỉ có thể gửi thông báo cho khách đang thuê phòng của mình");
        }
        return ResponseEntity.ok(notificationService.createNotification(request.getCustomerId(), request.getTitle(), request.getContent(), request.getType()==null?"GENERAL":request.getType()));
    }
}
