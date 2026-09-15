/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.service;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.InvoiceCreateRequest;
import com.nldv.rentalroom.dto.InvoiceDetailRequest;
import com.nldv.rentalroom.dto.InvoiceDetailResponse;
import com.nldv.rentalroom.dto.InvoiceResponse;
import com.nldv.rentalroom.enums.ContractStatus;
import com.nldv.rentalroom.pojo.Contract;
import com.nldv.rentalroom.pojo.Invoice;
import com.nldv.rentalroom.pojo.InvoiceDetail;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.pojo.Service;
import com.nldv.rentalroom.repository.ContractRepository;
import com.nldv.rentalroom.repository.InvoiceDetailRepository;
import com.nldv.rentalroom.repository.InvoiceRepository;
import com.nldv.rentalroom.repository.ServiceRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@org.springframework.stereotype.Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ContractRepository contractRepository;
    private final ServiceRepository serviceRepository;
    private final NotificationService notificationService;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            InvoiceDetailRepository invoiceDetailRepository,
            ContractRepository contractRepository,
            ServiceRepository serviceRepository,
            NotificationService notificationService) {

        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.contractRepository = contractRepository;
        this.serviceRepository = serviceRepository;
        this.notificationService = notificationService;
    }

    // =========================================================
    // LANDLORD - CREATE
    // =========================================================
    @Transactional
    public InvoiceResponse createInvoice(
            Integer landlordId,
            InvoiceCreateRequest request) {

        Contract contract
                = contractRepository.findById(
                        request.getContractId()
                ).orElseThrow(()
                        -> new IllegalArgumentException(
                                "Không tìm thấy hợp đồng"));

        // -----------------------------------------
        // Quyền sở hữu
        // -----------------------------------------
        Room room = contract.getRoom();

        if (room == null
                || room.getLandlord() == null
                || !room.getLandlord()
                        .getId()
                        .equals(landlordId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền tạo hóa đơn "
                    + "cho hợp đồng này");
        }

        // -----------------------------------------
        // Chỉ contract ACTIVE
        // -----------------------------------------
        if (contract.getStatus()
                != ContractStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Chỉ có thể tạo hóa đơn "
                    + "cho hợp đồng ACTIVE");
        }

        // -----------------------------------------
        // Kiểm tra ngày
        // -----------------------------------------
        LocalDate billingDate
                = request.getBillingDate();

        LocalDate dueDate
                = request.getDueDate();

        if (billingDate == null
                || dueDate == null) {

            throw new IllegalArgumentException(
                    "Ngày lập hóa đơn và ngày đến hạn "
                    + "không được để trống");
        }

        if (dueDate.isBefore(billingDate)) {

            throw new IllegalArgumentException(
                    "Ngày đến hạn không được trước "
                    + "ngày lập hóa đơn");
        }

        if (billingDate.isBefore(
                contract.getStartDate())) {

            throw new IllegalArgumentException(
                    "Ngày lập hóa đơn không được trước "
                    + "ngày bắt đầu hợp đồng");
        }

        if (billingDate.isAfter(
                contract.getEndDate())) {

            throw new IllegalArgumentException(
                    "Ngày lập hóa đơn không được sau "
                    + "ngày kết thúc hợp đồng");
        }

        // -----------------------------------------
        // Không tạo trùng kỳ
        // -----------------------------------------
        if (invoiceRepository
                .existsByContractIdAndBillingDate(
                        contract.getId(),
                        billingDate)) {

            throw new IllegalArgumentException(
                    "Hợp đồng này đã có hóa đơn "
                    + "trong ngày lập hóa đơn này");
        }

        // -----------------------------------------
        // Details phải có
        // -----------------------------------------
        if (request.getDetails() == null
                || request.getDetails().isEmpty()) {

            throw new IllegalArgumentException(
                    "Hóa đơn phải có ít nhất một "
                    + "chi tiết dịch vụ");
        }

        // -----------------------------------------
        // Kiểm tra service bị trùng
        // -----------------------------------------
        Set<Integer> serviceIds
                = new HashSet<>();

        int totalServiceAmount = 0;

        List<InvoiceDetailRequest> details
                = request.getDetails();

        for (InvoiceDetailRequest detailRequest
                : details) {

            if (!serviceIds.add(
                    detailRequest.getServiceId())) {

                throw new IllegalArgumentException(
                        "Một dịch vụ không được xuất hiện "
                        + "nhiều lần trong cùng hóa đơn");
            }

            if (detailRequest.getQuantity() == null
                    || detailRequest.getQuantity() <= 0) {

                throw new IllegalArgumentException(
                        "Số lượng dịch vụ phải lớn hơn 0");
            }

            if (detailRequest.getUnitPrice() == null
                    || detailRequest.getUnitPrice() < 0) {

                throw new IllegalArgumentException(
                        "Đơn giá dịch vụ không hợp lệ");
            }

            Service service
                    = serviceRepository.findById(
                            detailRequest.getServiceId()
                    ).orElseThrow(()
                            -> new IllegalArgumentException(
                                    "Không tìm thấy dịch vụ ID: "
                                    + detailRequest.getServiceId()));

            int amount
                    = detailRequest.getQuantity()
                    * detailRequest.getUnitPrice();

            totalServiceAmount += amount;
        }

        // -----------------------------------------
        // Tính tổng
        // -----------------------------------------
        int monthlyRent
                = contract.getMonthlyRent();

        int totalAmount
                = monthlyRent + totalServiceAmount;

        // -----------------------------------------
        // Tạo Invoice
        // -----------------------------------------
        Invoice invoice
                = new Invoice();

        invoice.setContract(contract);
        invoice.setBillingDate(billingDate);
        invoice.setDueDate(dueDate);
        invoice.setTotalAmount(totalAmount);

        Invoice savedInvoice
                = invoiceRepository.save(invoice);

        // -----------------------------------------
        // Tạo Invoice Details
        // -----------------------------------------
        for (InvoiceDetailRequest detailRequest
                : details) {

            Service service
                    = serviceRepository.findById(
                            detailRequest.getServiceId()
                    ).orElseThrow(()
                            -> new IllegalArgumentException(
                                    "Không tìm thấy dịch vụ ID: "
                                    + detailRequest.getServiceId()));

            int amount
                    = detailRequest.getQuantity()
                    * detailRequest.getUnitPrice();

            InvoiceDetail detail
                    = new InvoiceDetail();

            detail.setInvoice(savedInvoice);
            detail.setService(service);
            detail.setQuantity(
                    detailRequest.getQuantity());
            detail.setUnitPrice(
                    detailRequest.getUnitPrice());
            detail.setAmount(amount);

            invoiceDetailRepository.save(detail);
        }

        // -----------------------------------------
        // Notification
        // -----------------------------------------
        notificationService.createNotification(
                contract.getUser().getId(),
                "Hóa đơn mới",
                "Bạn có hóa đơn mới cho phòng "
                + room.getRoomNumber()
                + ". Tổng tiền: "
                + totalAmount,
                "INVOICE"
        );

        return toResponse(savedInvoice);
    }

    // =========================================================
    // LANDLORD - LIST
    // =========================================================
    public List<InvoiceResponse> getLandlordInvoices(
            Integer landlordId) {

        return invoiceRepository
                .findByContractRoomLandlordId(landlordId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // LANDLORD - DETAIL
    // =========================================================
    public InvoiceResponse getLandlordInvoice(
            Integer landlordId,
            Integer invoiceId) {

        Invoice invoice
                = invoiceRepository.findById(invoiceId)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "Không tìm thấy hóa đơn"));

        Room room
                = invoice.getContract().getRoom();

        if (room == null
                || room.getLandlord() == null
                || !room.getLandlord()
                        .getId()
                        .equals(landlordId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền xem hóa đơn này");
        }

        return toResponse(invoice);
    }

    // =========================================================
    // CUSTOMER - LIST
    // =========================================================
    public List<InvoiceResponse> getCustomerInvoices(
            Integer customerId) {

        return invoiceRepository
                .findByContractUserId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // CUSTOMER - DETAIL
    // =========================================================
    public InvoiceResponse getCustomerInvoice(
            Integer customerId,
            Integer invoiceId) {

        Invoice invoice
                = invoiceRepository.findById(invoiceId)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "Không tìm thấy hóa đơn"));

        if (invoice.getContract() == null
                || invoice.getContract().getUser() == null
                || !invoice.getContract()
                        .getUser()
                        .getId()
                        .equals(customerId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền xem hóa đơn này");
        }

        return toResponse(invoice);
    }

    // =========================================================
    // RESPONSE
    // =========================================================
    private InvoiceResponse toResponse(
            Invoice invoice) {

        Contract contract
                = invoice.getContract();

        Room room
                = contract.getRoom();

        InvoiceResponse response
                = new InvoiceResponse();

        response.setId(invoice.getId());

        response.setContractId(
                contract.getId());

        response.setCustomerId(
                contract.getUser().getId());

        response.setCustomerUsername(
                contract.getUser().getUsername());

        response.setRoomId(room.getId());

        response.setRoomNumber(
                room.getRoomNumber());

        response.setBillingDate(
                invoice.getBillingDate());

        response.setDueDate(
                invoice.getDueDate());

        response.setMonthlyRent(
                contract.getMonthlyRent());

        List<InvoiceDetail> details
                = invoiceDetailRepository
                        .findByInvoiceId(
                                invoice.getId());

        List<InvoiceDetailResponse> detailResponses
                = new ArrayList<>();

        int totalServiceAmount = 0;

        for (InvoiceDetail detail : details) {

            InvoiceDetailResponse detailResponse
                    = new InvoiceDetailResponse();

            detailResponse.setId(
                    detail.getId());

            detailResponse.setServiceId(
                    detail.getService().getId());

            detailResponse.setServiceName(
                    detail.getService().getName());

            detailResponse.setUnit(
                    detail.getService().getUnit());

            detailResponse.setQuantity(
                    detail.getQuantity());

            detailResponse.setUnitPrice(
                    detail.getUnitPrice());

            detailResponse.setAmount(
                    detail.getAmount());

            detailResponses.add(
                    detailResponse);

            totalServiceAmount
                    += detail.getAmount();
        }

        response.setDetails(
                detailResponses);

        response.setTotalServiceAmount(
                totalServiceAmount);

        response.setTotalAmount(
                contract.getMonthlyRent()
                + totalServiceAmount);

        response.setCreatedDate(
                invoice.getCreatedDate());

        response.setUpdatedDate(
                invoice.getUpdatedDate());

        return response;
    }
}
