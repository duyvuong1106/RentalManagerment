package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
}