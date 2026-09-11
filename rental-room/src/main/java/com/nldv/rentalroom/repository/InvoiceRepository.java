package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}