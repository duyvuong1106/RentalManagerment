package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Invoice;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    
    List<Invoice> findByContractUserId(Integer userId);

    List<Invoice> findByContractRoomLandlordId(Integer landlordId);

    List<Invoice> findByContractId(Integer contractId);

    boolean existsByContractIdAndBillingDate(
            Integer contractId,
            LocalDate billingDate
    );
}