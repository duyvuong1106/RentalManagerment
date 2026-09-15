/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.service;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.PaymentCreateRequest;
import com.nldv.rentalroom.dto.PaymentResponse;
import com.nldv.rentalroom.enums.PaymentMethod;
import com.nldv.rentalroom.enums.PaymentStatus;
import com.nldv.rentalroom.pojo.Invoice;
import com.nldv.rentalroom.pojo.Payment;
import com.nldv.rentalroom.repository.InvoiceRepository;
import com.nldv.rentalroom.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final NotificationService notificationService;

    public PaymentService(
            PaymentRepository paymentRepository,
            InvoiceRepository invoiceRepository,
            NotificationService notificationService) {

        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public PaymentResponse createPayment(
            Integer customerId,
            PaymentCreateRequest request) {

        Invoice invoice
                = invoiceRepository.findById(
                        request.getInvoiceId()
                ).orElseThrow(()
                        -> new IllegalArgumentException(
                                "Không tìm thấy hóa đơn"));

        if (invoice.getContract() == null
                || invoice.getContract().getUser() == null
                || !invoice.getContract()
                        .getUser()
                        .getId()
                        .equals(customerId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền thanh toán "
                    + "hóa đơn này");
        }

        if (paymentRepository
                .existsByInvoiceIdAndStatus(
                        invoice.getId(),
                        PaymentStatus.SUCCESS)) {

            throw new IllegalArgumentException(
                    "Hóa đơn này đã được thanh toán");
        }

        if (paymentRepository
                .existsByInvoiceIdAndStatus(
                        invoice.getId(),
                        PaymentStatus.PENDING)) {

            throw new IllegalArgumentException(
                    "Hóa đơn này đang có giao dịch "
                    + "chờ xử lý");
        }

        Long paidAmount
                = paymentRepository
                        .sumAmountByInvoiceIdAndStatus(
                                invoice.getId(),
                                PaymentStatus.SUCCESS);

        int alreadyPaid
                = paidAmount == null
                        ? 0
                        : paidAmount.intValue();

        int remainingAmount
                = invoice.getTotalAmount()
                - alreadyPaid;

        if (remainingAmount <= 0) {

            throw new IllegalArgumentException(
                    "Hóa đơn không còn số tiền "
                    + "cần thanh toán");
        }

        if (request.getAmount() == null
                || request.getAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Số tiền thanh toán phải lớn hơn 0");
        }

        if (!request.getAmount()
                .equals(remainingAmount)) {

            throw new IllegalArgumentException(
                    "Số tiền thanh toán phải là "
                    + remainingAmount);
        }

        PaymentMethod method
                = request.getPaymentMethod();

        if (method == null) {

            throw new IllegalArgumentException(
                    "Phương thức thanh toán "
                    + "không được để trống");
        }

        if (method == PaymentMethod.ONLINE) {

            if (request.getTransactionCode() == null
                    || request.getTransactionCode()
                            .isBlank()) {

                throw new IllegalArgumentException(
                        "Thanh toán ONLINE phải có "
                        + "transactionCode");
            }
        }

        if (request.getTransactionCode() != null
                && !request.getTransactionCode()
                        .isBlank()
                && paymentRepository
                        .existsByTransactionCode(
                                request.getTransactionCode())) {

            throw new IllegalArgumentException(
                    "Transaction code đã tồn tại");
        }

        Payment payment
                = new Payment();

        payment.setInvoice(invoice);

        payment.setAmount(
                request.getAmount());

        payment.setPaymentMethod(
                method);

        payment.setTransactionCode(
                request.getTransactionCode());

        payment.setStatus(
                PaymentStatus.PENDING);

        payment.setPaidDate(null);

        Payment saved
                = paymentRepository.save(payment);

        if (invoice.getContract()
                .getRoom()
                .getLandlord() != null) {

            notificationService.createNotification(
                    invoice.getContract()
                            .getRoom()
                            .getLandlord()
                            .getId(),
                    "Có yêu cầu thanh toán mới",
                    "Khách hàng "
                    + invoice.getContract()
                            .getUser()
                            .getUsername()
                    + " đã tạo yêu cầu thanh toán "
                    + "cho hóa đơn #"
                    + invoice.getId(),
                    "PAYMENT"
            );
        }

        return toResponse(saved);
    }

    public List<PaymentResponse>
            getCustomerPayments(
                    Integer customerId) {

        return paymentRepository
                .findByInvoiceContractUserId(
                        customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PaymentResponse getCustomerPayment(
            Integer customerId,
            Integer paymentId) {

        Payment payment
                = findById(paymentId);

        if (payment.getInvoice()
                .getContract()
                .getUser()
                .getId()
                .equals(customerId) == false) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền xem "
                    + "thanh toán này");
        }

        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse cancelPayment(
            Integer customerId,
            Integer paymentId) {

        Payment payment
                = findById(paymentId);

        if (!payment.getInvoice()
                .getContract()
                .getUser()
                .getId()
                .equals(customerId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền hủy "
                    + "thanh toán này");
        }

        if (payment.getStatus()
                != PaymentStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Chỉ có thể hủy "
                    + "thanh toán PENDING");
        }

        payment.setStatus(
                PaymentStatus.CANCELLED);

        payment.setPaidDate(null);

        Payment saved
                = paymentRepository.save(payment);

        notificationService.createNotification(
                payment.getInvoice()
                        .getContract()
                        .getRoom()
                        .getLandlord()
                        .getId(),
                "Thanh toán đã bị hủy",
                "Khách hàng "
                + payment.getInvoice()
                        .getContract()
                        .getUser()
                        .getUsername()
                + " đã hủy thanh toán cho hóa đơn #"
                + payment.getInvoice().getId(),
                "PAYMENT"
        );

        return toResponse(saved);
    }

    public List<PaymentResponse>
            getLandlordPayments(
                    Integer landlordId) {

        return paymentRepository
                .findByInvoiceContractRoomLandlordId(
                        landlordId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PaymentResponse getLandlordPayment(
            Integer landlordId,
            Integer paymentId) {

        Payment payment
                = findById(paymentId);

        checkLandlordOwnership(
                payment,
                landlordId);

        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse confirmPayment(
            Integer landlordId,
            Integer paymentId) {

        Payment payment
                = findById(paymentId);

        checkLandlordOwnership(
                payment,
                landlordId);

        if (payment.getStatus()
                != PaymentStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Chỉ có thể xác nhận "
                    + "thanh toán PENDING");
        }

        Invoice invoice
                = payment.getInvoice();

        if (paymentRepository
                .existsByInvoiceIdAndStatus(
                        invoice.getId(),
                        PaymentStatus.SUCCESS)) {

            throw new IllegalArgumentException(
                    "Hóa đơn này đã có "
                    + "thanh toán thành công");
        }

        Long paidAmount
                = paymentRepository
                        .sumAmountByInvoiceIdAndStatus(
                                invoice.getId(),
                                PaymentStatus.SUCCESS);

        int currentPaid
                = paidAmount == null
                        ? 0
                        : paidAmount.intValue();

        int remaining
                = invoice.getTotalAmount()
                - currentPaid;

        if (payment.getAmount()
                > remaining) {

            throw new IllegalArgumentException(
                    "Số tiền thanh toán vượt quá "
                    + "số tiền còn lại");
        }

        if (payment.getAmount()
                < remaining) {

            throw new IllegalArgumentException(
                    "Hệ thống hiện chỉ hỗ trợ "
                    + "thanh toán đủ toàn bộ hóa đơn");
        }

        payment.setStatus(
                PaymentStatus.SUCCESS);

        payment.setPaidDate(
                LocalDateTime.now());

        Payment saved
                = paymentRepository.save(payment);

        notificationService.createNotification(
                invoice.getContract()
                        .getUser()
                        .getId(),
                "Thanh toán thành công",
                "Thanh toán cho hóa đơn #"
                + invoice.getId()
                + " đã được xác nhận thành công.",
                "PAYMENT"
        );

        return toResponse(saved);
    }

    @Transactional
    public PaymentResponse rejectPayment(
            Integer landlordId,
            Integer paymentId) {

        Payment payment
                = findById(paymentId);

        checkLandlordOwnership(
                payment,
                landlordId);

        if (payment.getStatus()
                != PaymentStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Chỉ có thể từ chối "
                    + "thanh toán PENDING");
        }

        payment.setStatus(
                PaymentStatus.FAILED);

        payment.setPaidDate(null);

        Payment saved
                = paymentRepository.save(payment);

        notificationService.createNotification(
                payment.getInvoice()
                        .getContract()
                        .getUser()
                        .getId(),
                "Thanh toán bị từ chối",
                "Thanh toán cho hóa đơn #"
                + payment.getInvoice().getId()
                + " đã bị từ chối.",
                "PAYMENT"
        );

        return toResponse(saved);
    }

    private Payment findById(
            Integer paymentId) {

        return paymentRepository
                .findById(paymentId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy thanh toán"));
    }

    private void checkLandlordOwnership(
            Payment payment,
            Integer landlordId) {

        Integer ownerId
                = payment.getInvoice()
                        .getContract()
                        .getRoom()
                        .getLandlord()
                        .getId();

        if (!ownerId.equals(landlordId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền xử lý "
                    + "thanh toán này");
        }
    }

    private PaymentResponse toResponse(
            Payment payment) {

        Invoice invoice
                = payment.getInvoice();

        Long paidAmountValue
                = paymentRepository
                        .sumAmountByInvoiceIdAndStatus(
                                invoice.getId(),
                                PaymentStatus.SUCCESS);

        int paidAmount
                = paidAmountValue == null
                        ? 0
                        : paidAmountValue.intValue();

        int remainingAmount
                = Math.max(
                        0,
                        invoice.getTotalAmount()
                        - paidAmount);

        PaymentResponse response
                = new PaymentResponse();

        response.setId(
                payment.getId());

        response.setInvoiceId(
                invoice.getId());

        response.setCustomerId(
                invoice.getContract()
                        .getUser()
                        .getId());

        response.setCustomerUsername(
                invoice.getContract()
                        .getUser()
                        .getUsername());

        response.setRoomId(
                invoice.getContract()
                        .getRoom()
                        .getId());

        response.setRoomNumber(
                invoice.getContract()
                        .getRoom()
                        .getRoomNumber());

        response.setInvoiceTotalAmount(
                invoice.getTotalAmount());

        response.setPaidAmount(
                paidAmount);

        response.setRemainingAmount(
                remainingAmount);

        response.setAmount(
                payment.getAmount());

        response.setPaymentMethod(
                payment.getPaymentMethod());

        response.setStatus(
                payment.getStatus());

        response.setTransactionCode(
                payment.getTransactionCode());

        response.setPaidDate(
                payment.getPaidDate());

        response.setCreatedDate(
                payment.getCreatedDate());

        response.setUpdatedDate(
                payment.getUpdatedDate());

        return response;
    }
}
