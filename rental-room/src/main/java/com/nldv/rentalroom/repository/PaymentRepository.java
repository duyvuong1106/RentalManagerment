package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.enums.PaymentStatus;
import com.nldv.rentalroom.pojo.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
    List<Payment> findByInvoiceId(Integer invoiceId);

    List<Payment> findByInvoiceContractUserId(Integer userId);

    List<Payment> findByInvoiceContractRoomLandlordId(
            Integer landlordId);

    boolean existsByInvoiceIdAndStatus(
            Integer invoiceId,
            PaymentStatus status);

    boolean existsByTransactionCode(String transactionCode);

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.invoice.id = :invoiceId
        AND p.status = :status
    """)
    Long sumAmountByInvoiceIdAndStatus(
            @Param("invoiceId") Integer invoiceId,
            @Param("status") PaymentStatus status);
    
}