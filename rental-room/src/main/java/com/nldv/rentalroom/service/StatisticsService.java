package com.nldv.rentalroom.service;

import com.nldv.rentalroom.dto.RevenuePointResponse;
import com.nldv.rentalroom.dto.StatisticsResponse;
import com.nldv.rentalroom.enums.ContractStatus;
import com.nldv.rentalroom.enums.PaymentStatus;
import com.nldv.rentalroom.enums.RentalRequestStatus;
import com.nldv.rentalroom.enums.RoomStatus;
import com.nldv.rentalroom.enums.UserRole;
import com.nldv.rentalroom.pojo.Contract;
import com.nldv.rentalroom.pojo.Invoice;
import com.nldv.rentalroom.pojo.Payment;
import com.nldv.rentalroom.repository.ContractRepository;
import com.nldv.rentalroom.repository.InvoiceRepository;
import com.nldv.rentalroom.repository.PaymentRepository;
import com.nldv.rentalroom.repository.RentalRequestRepository;
import com.nldv.rentalroom.repository.ReviewRepository;
import com.nldv.rentalroom.repository.RoomRepository;
import com.nldv.rentalroom.repository.UserRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {
    private final RoomRepository roomRepository;
    private final ContractRepository contractRepository;
    private final RentalRequestRepository rentalRequestRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public StatisticsService(RoomRepository roomRepository, ContractRepository contractRepository,
            RentalRequestRepository rentalRequestRepository, InvoiceRepository invoiceRepository,
            PaymentRepository paymentRepository, ReviewRepository reviewRepository,
            UserRepository userRepository) {
        this.roomRepository=roomRepository; this.contractRepository=contractRepository;
        this.rentalRequestRepository=rentalRequestRepository; this.invoiceRepository=invoiceRepository;
        this.paymentRepository=paymentRepository; this.reviewRepository=reviewRepository; this.userRepository=userRepository;
    }

    public StatisticsResponse landlord(Integer landlordId) {
        StatisticsResponse r = new StatisticsResponse();
        long total = roomRepository.countByLandlordId(landlordId);
        long available = roomRepository.countByLandlordIdAndStatus(landlordId, RoomStatus.AVAILABLE);
        long pending = roomRepository.countByLandlordIdAndStatus(landlordId, RoomStatus.PENDING);
        long rented = roomRepository.countByLandlordIdAndStatus(landlordId, RoomStatus.RENTED);
        List<Contract> contracts = contractRepository.findByRoomLandlordId(landlordId);
        List<Invoice> invoices = invoiceRepository.findByContractRoomLandlordId(landlordId);
        List<Payment> payments = paymentRepository.findByInvoiceContractRoomLandlordId(landlordId);
        long paid = payments.stream().filter(p -> p.getStatus()==PaymentStatus.SUCCESS).mapToLong(p -> p.getAmount()==null?0:p.getAmount()).sum();
        long invoiceTotal = invoices.stream().mapToLong(i -> i.getTotalAmount()==null?0:i.getTotalAmount()).sum();
        r.setTotalRooms(total); r.setAvailableRooms(available); r.setPendingRooms(pending); r.setRentedRooms(rented);
        r.setTotalCustomers(contracts.stream().map(c -> c.getUser()==null?null:c.getUser().getId()).filter(x -> x!=null).distinct().count());
        r.setPendingRentalRequests(rentalRequestRepository.countByRoomLandlordIdAndStatus(landlordId, RentalRequestStatus.PENDING));
        r.setActiveContracts(contracts.stream().filter(c -> c.getStatus()==ContractStatus.ACTIVE).count());
        LocalDate threshold = LocalDate.now().plusDays(30);
        r.setExpiringContracts(contracts.stream().filter(c -> c.getStatus()==ContractStatus.ACTIVE && c.getEndDate()!=null && !c.getEndDate().isBefore(LocalDate.now()) && !c.getEndDate().isAfter(threshold)).count());
        r.setTotalInvoices(invoices.size()); r.setTotalPayments(payments.size());
        r.setSuccessfulPayments(payments.stream().filter(p -> p.getStatus()==PaymentStatus.SUCCESS).count());
        r.setPendingPayments(payments.stream().filter(p -> p.getStatus()==PaymentStatus.PENDING).count());
        r.setTotalRevenue(paid); r.setPaidAmount(paid); r.setUnpaidAmount(Math.max(0, invoiceTotal-paid));
        r.setOccupancyRate(total == 0 ? 0 : Math.round((rented * 10000.0 / total))/100.0);
        Double avg = reviewRepository.averageVisibleRatingByLandlordId(landlordId);
        r.setAverageRating(avg == null ? 0 : Math.round(avg*100.0)/100.0);
        r.setTotalReviews(reviewRepository.findAll().stream().filter(v -> v.getRoom()!=null && v.getRoom().getLandlord()!=null && landlordId.equals(v.getRoom().getLandlord().getId())).count());
        return r;
    }

    public StatisticsResponse admin() {
        StatisticsResponse r = new StatisticsResponse();
        long total = roomRepository.count();
        long available = roomRepository.countByStatus(RoomStatus.AVAILABLE);
        long pending = roomRepository.countByStatus(RoomStatus.PENDING);
        long rented = roomRepository.countByStatus(RoomStatus.RENTED);
        List<Invoice> invoices = invoiceRepository.findAll();
        List<Payment> payments = paymentRepository.findAll();
        long paid = payments.stream().filter(p -> p.getStatus()==PaymentStatus.SUCCESS).mapToLong(p -> p.getAmount()==null?0:p.getAmount()).sum();
        long invoiceTotal = invoices.stream().mapToLong(i -> i.getTotalAmount()==null?0:i.getTotalAmount()).sum();
        r.setTotalRooms(total); r.setAvailableRooms(available); r.setPendingRooms(pending); r.setRentedRooms(rented);
        r.setTotalCustomers(userRepository.findAll().stream().filter(u -> u.getRole()==UserRole.CUSTOMER).count());
        r.setPendingRentalRequests(rentalRequestRepository.countByStatus(RentalRequestStatus.PENDING));
        r.setActiveContracts(contractRepository.countByStatus(ContractStatus.ACTIVE));
        r.setExpiringContracts(contractRepository.findAll().stream().filter(c -> c.getStatus()==ContractStatus.ACTIVE && c.getEndDate()!=null && !c.getEndDate().isBefore(LocalDate.now()) && !c.getEndDate().isAfter(LocalDate.now().plusDays(30))).count());
        r.setTotalInvoices(invoices.size()); r.setTotalPayments(payments.size());
        r.setSuccessfulPayments(payments.stream().filter(p -> p.getStatus()==PaymentStatus.SUCCESS).count());
        r.setPendingPayments(payments.stream().filter(p -> p.getStatus()==PaymentStatus.PENDING).count());
        r.setTotalRevenue(paid); r.setPaidAmount(paid); r.setUnpaidAmount(Math.max(0, invoiceTotal-paid));
        r.setOccupancyRate(total == 0 ? 0 : Math.round((rented * 10000.0 / total))/100.0);
        Double avg = reviewRepository.averageVisibleRating(); r.setAverageRating(avg == null ? 0 : Math.round(avg*100.0)/100.0);
        r.setTotalReviews(reviewRepository.countByIsVisibleTrue());
        return r;
    }

    public List<RevenuePointResponse> landlordRevenue(Integer landlordId, int year) {
        return aggregate(paymentRepository.findByInvoiceContractRoomLandlordId(landlordId), year);
    }

    public List<RevenuePointResponse> adminRevenue(int year) {
        return aggregate(paymentRepository.findAll(), year);
    }

    private List<RevenuePointResponse> aggregate(List<Payment> payments, int year) {
        Map<Integer, Long> sums = new HashMap<>();
        for (Payment p : payments) {
            if (p.getStatus() != PaymentStatus.SUCCESS || p.getPaidDate() == null || p.getPaidDate().getYear() != year) continue;
            sums.merge(p.getPaidDate().getMonthValue(), p.getAmount()==null?0:p.getAmount().longValue(), Long::sum);
        }
        return java.util.stream.IntStream.rangeClosed(1,12)
                .mapToObj(m -> new RevenuePointResponse(String.format("%d-%02d", year, m), sums.getOrDefault(m,0L)))
                .toList();
    }
}
