package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {
}