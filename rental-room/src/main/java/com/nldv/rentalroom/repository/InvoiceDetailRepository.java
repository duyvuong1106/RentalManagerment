package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.InvoiceDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {

    List<InvoiceDetail> findByInvoiceId(Integer invoiceId);

    void deleteByInvoiceId(Integer invoiceId);
}
