package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.enums.ContractStatus;
import com.nldv.rentalroom.pojo.Contract;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository
        extends JpaRepository<Contract, Integer> {

    List<Contract> findByUserId(Integer userId);

    List<Contract> findByRoomId(Integer roomId);

    List<Contract> findByRoomLandlordId(
            Integer landlordId);

    boolean existsByRoomIdAndStatus(
            Integer roomId,
            ContractStatus status
    );

    boolean existsByUserIdAndRoomIdAndStatus(
            Integer userId,
            Integer roomId,
            ContractStatus status
    );

    boolean existsByUserIdAndRoomLandlordIdAndStatus(Integer userId, Integer landlordId, ContractStatus status);

    long countByStatus(ContractStatus status);

    long countByRoomLandlordIdAndStatus(Integer landlordId, ContractStatus status);

    List<Contract> findByStatusAndEndDateBefore(
            ContractStatus status,
            LocalDate date
    );
}
