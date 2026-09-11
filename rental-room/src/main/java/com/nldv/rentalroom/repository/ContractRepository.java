package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
}