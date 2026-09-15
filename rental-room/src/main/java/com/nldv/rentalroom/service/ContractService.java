/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.service;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.ContractCreateRequest;
import com.nldv.rentalroom.dto.ContractResponse;
import com.nldv.rentalroom.enums.ContractStatus;
import com.nldv.rentalroom.enums.RentalRequestStatus;
import com.nldv.rentalroom.enums.RoomStatus;
import com.nldv.rentalroom.pojo.Contract;
import com.nldv.rentalroom.pojo.RentalRequest;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.repository.ContractRepository;
import com.nldv.rentalroom.repository.RentalRequestRepository;
import com.nldv.rentalroom.repository.RoomRepository;
import java.time.LocalDate;
import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final RoomRepository roomRepository;
    private final RentalRequestRepository rentalRequestRepository;
    private final NotificationService notificationService;

    public ContractService(
            ContractRepository contractRepository,
            RoomRepository roomRepository,
            RentalRequestRepository rentalRequestRepository,
            NotificationService notificationService) {

        this.contractRepository = contractRepository;
        this.roomRepository = roomRepository;
        this.rentalRequestRepository
                = rentalRequestRepository;
        this.notificationService
                = notificationService;
    }

    // =========================================================
    // LANDLORD - CREATE CONTRACT
    // =========================================================
    @Transactional
    public ContractResponse createContract(
            Integer landlordId,
            ContractCreateRequest request) {

        RentalRequest rentalRequest
                = rentalRequestRepository
                        .findById(request.getRentalRequestId())
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "Không tìm thấy yêu cầu thuê"));

        Room room = rentalRequest.getRoom();

        // -----------------------------------------
        // Kiểm tra quyền sở hữu phòng
        // -----------------------------------------
        checkLandlordOwnership(
                room,
                landlordId);

        // -----------------------------------------
        // RentalRequest phải APPROVED
        // -----------------------------------------
        if (rentalRequest.getStatus()
                != RentalRequestStatus.APPROVED) {

            throw new IllegalArgumentException(
                    "Chỉ có yêu cầu thuê đã được chấp nhận "
                    + "mới được tạo hợp đồng");
        }

        User customer = rentalRequest.getUser();

        if (customer == null) {

            throw new IllegalArgumentException(
                    "Yêu cầu thuê chưa có khách hàng");
        }

        // -----------------------------------------
        // Room phải PENDING
        // -----------------------------------------
        if (room.getStatus()
                != RoomStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Phòng không ở trạng thái PENDING");
        }

        // -----------------------------------------
        // Kiểm tra ngày
        // -----------------------------------------
        validateDates(
                request.getStartDate(),
                request.getEndDate());

        // Không cho bắt đầu trong quá khứ
        if (request.getStartDate()
                .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Ngày bắt đầu hợp đồng không được ở quá khứ");
        }

        // -----------------------------------------
        // Kiểm tra tiền
        // -----------------------------------------
        if (request.getMonthlyRent() == null
                || request.getMonthlyRent() <= 0) {

            throw new IllegalArgumentException(
                    "Tiền thuê hàng tháng phải lớn hơn 0");
        }

        if (request.getDeposit() == null
                || request.getDeposit() < 0) {

            throw new IllegalArgumentException(
                    "Tiền cọc không được âm");
        }

        // -----------------------------------------
        // Không được có Contract ACTIVE
        // -----------------------------------------
        boolean hasActiveContract
                = contractRepository
                        .existsByRoomIdAndStatus(
                                room.getId(),
                                ContractStatus.ACTIVE);

        if (hasActiveContract) {

            throw new IllegalArgumentException(
                    "Phòng đã có hợp đồng ACTIVE");
        }

        // -----------------------------------------
        // Không được có Contract DRAFT
        // -----------------------------------------
        boolean hasDraftContract
                = contractRepository
                        .existsByRoomIdAndStatus(
                                room.getId(),
                                ContractStatus.DRAFT);

        if (hasDraftContract) {

            throw new IllegalArgumentException(
                    "Phòng đã có hợp đồng DRAFT");
        }

        // -----------------------------------------
        // Tạo contract
        // -----------------------------------------
        Contract contract
                = new Contract();

        contract.setUser(customer);
        contract.setRoom(room);

        contract.setStartDate(
                request.getStartDate());

        contract.setEndDate(
                request.getEndDate());

        contract.setMonthlyRent(
                request.getMonthlyRent());

        contract.setDeposit(
                request.getDeposit());

        contract.setContractUrl(
                request.getContractUrl());

        contract.setStatus(
                ContractStatus.DRAFT);

        Contract saved
                = contractRepository.save(contract);

        // -----------------------------------------
        // Thông báo khách hàng
        // -----------------------------------------
        notificationService.createNotification(
                customer.getId(),
                "Hợp đồng thuê phòng đã được tạo",
                "Hợp đồng thuê phòng "
                + room.getRoomNumber()
                + " đã được tạo. "
                + "Vui lòng kiểm tra thông tin hợp đồng.",
                "CONTRACT"
        );

        return convertToResponse(saved);
    }

    // =========================================================
    // CUSTOMER - LIST
    // =========================================================
    public List<ContractResponse> getCustomerContracts(
            Integer customerId) {

        return contractRepository
                .findByUserId(customerId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =========================================================
    // CUSTOMER - DETAIL
    // =========================================================
    public ContractResponse getCustomerContract(
            Integer customerId,
            Integer contractId) {

        Contract contract
                = findById(contractId);

        if (contract.getUser() == null
                || !contract.getUser()
                        .getId()
                        .equals(customerId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền xem hợp đồng này");
        }

        return convertToResponse(contract);
    }

    // =========================================================
    // LANDLORD - LIST
    // =========================================================
    public List<ContractResponse> getLandlordContracts(
            Integer landlordId) {

        return contractRepository
                .findByRoomLandlordId(landlordId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =========================================================
    // LANDLORD - DETAIL
    // =========================================================
    public ContractResponse getLandlordContract(
            Integer landlordId,
            Integer contractId) {

        Contract contract
                = findById(contractId);

        checkLandlordOwnership(
                contract.getRoom(),
                landlordId);

        return convertToResponse(contract);
    }

    // =========================================================
    // LANDLORD - ACTIVATE
    // =========================================================
    @Transactional
    public ContractResponse activateContract(
            Integer landlordId,
            Integer contractId) {

        Contract contract
                = findById(contractId);

        checkLandlordOwnership(
                contract.getRoom(),
                landlordId);

        // -----------------------------------------
        // Chỉ DRAFT mới được ACTIVE
        // -----------------------------------------
        if (contract.getStatus()
                != ContractStatus.DRAFT) {

            throw new IllegalArgumentException(
                    "Chỉ có thể kích hoạt hợp đồng DRAFT");
        }

        LocalDate today = LocalDate.now();

        // -----------------------------------------
        // Không cho start date trong tương lai
        // -----------------------------------------
        if (contract.getStartDate()
                .isAfter(today)) {

            throw new IllegalArgumentException(
                    "Chưa đến ngày bắt đầu hợp đồng");
        }

        // -----------------------------------------
        // Không cho activate hợp đồng đã hết hạn
        // -----------------------------------------
        if (contract.getEndDate()
                .isBefore(today)) {

            contract.setStatus(
                    ContractStatus.EXPIRED);

            contractRepository.save(contract);

            throw new IllegalArgumentException(
                    "Hợp đồng đã hết hạn theo ngày kết thúc");
        }

        Room room
                = contract.getRoom();

        // -----------------------------------------
        // Room không được RENTED
        // -----------------------------------------
        if (room.getStatus()
                == RoomStatus.RENTED) {

            throw new IllegalArgumentException(
                    "Phòng đang được thuê");
        }

        // -----------------------------------------
        // Không có ACTIVE contract khác
        // -----------------------------------------
        boolean hasOtherActiveContract
                = contractRepository
                        .existsByRoomIdAndStatus(
                                room.getId(),
                                ContractStatus.ACTIVE);

        if (hasOtherActiveContract) {

            throw new IllegalArgumentException(
                    "Phòng đã có hợp đồng ACTIVE");
        }

        // -----------------------------------------
        // Activate
        // -----------------------------------------
        contract.setStatus(
                ContractStatus.ACTIVE);

        room.setStatus(
                RoomStatus.RENTED);

        roomRepository.save(room);

        Contract updated
                = contractRepository.save(contract);

        // -----------------------------------------
        // Notification
        // -----------------------------------------
        notificationService.createNotification(
                contract.getUser().getId(),
                "Hợp đồng đã được kích hoạt",
                "Hợp đồng thuê phòng "
                + room.getRoomNumber()
                + " đã được kích hoạt.",
                "CONTRACT"
        );

        return convertToResponse(updated);
    }

    // =========================================================
    // LANDLORD - TERMINATE
    // =========================================================
    @Transactional
    public ContractResponse terminateContract(
            Integer landlordId,
            Integer contractId) {

        Contract contract
                = findById(contractId);

        checkLandlordOwnership(
                contract.getRoom(),
                landlordId);

        if (contract.getStatus()
                != ContractStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Chỉ có thể chấm dứt hợp đồng ACTIVE");
        }

        contract.setStatus(
                ContractStatus.TERMINATED);

        Room room
                = contract.getRoom();

        room.setStatus(
                RoomStatus.AVAILABLE);

        roomRepository.save(room);

        Contract updated
                = contractRepository.save(contract);

        notificationService.createNotification(
                contract.getUser().getId(),
                "Hợp đồng đã chấm dứt",
                "Hợp đồng thuê phòng "
                + room.getRoomNumber()
                + " đã được chấm dứt.",
                "CONTRACT"
        );

        return convertToResponse(updated);
    }

    private Contract findById(
            Integer id) {

        return contractRepository
                .findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy hợp đồng"));
    }

    private void checkLandlordOwnership(
            Room room,
            Integer landlordId) {

        if (room == null
                || room.getLandlord() == null
                || room.getLandlord().getId() == null
                || !room.getLandlord()
                        .getId()
                        .equals(landlordId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền quản lý phòng này");
        }
    }

    private void validateDates(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate == null
                || endDate == null) {

            throw new IllegalArgumentException(
                    "Ngày bắt đầu và ngày kết thúc "
                    + "không được để trống");
        }

        if (!startDate.isBefore(endDate)) {

            throw new IllegalArgumentException(
                    "Ngày bắt đầu phải trước ngày kết thúc");
        }
    }

    private ContractResponse convertToResponse(
            Contract contract) {

        ContractResponse response
                = new ContractResponse();

        response.setId(
                contract.getId());

        response.setUserId(
                contract.getUser().getId());

        response.setUsername(
                contract.getUser().getUsername());

        response.setRoomId(
                contract.getRoom().getId());

        response.setRoomNumber(
                contract.getRoom().getRoomNumber());

        response.setStartDate(
                contract.getStartDate());

        response.setEndDate(
                contract.getEndDate());

        response.setMonthlyRent(
                contract.getMonthlyRent());

        response.setDeposit(
                contract.getDeposit());

        response.setContractUrl(
                contract.getContractUrl());

        response.setStatus(
                contract.getStatus());

        response.setCreatedDate(
                contract.getCreatedDate());

        response.setUpdatedDate(
                contract.getUpdatedDate());

        return response;
    }
}
