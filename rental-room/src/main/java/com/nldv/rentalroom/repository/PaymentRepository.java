package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}