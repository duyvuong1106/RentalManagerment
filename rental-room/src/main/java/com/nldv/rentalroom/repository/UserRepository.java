package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}