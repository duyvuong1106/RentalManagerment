/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.service;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.enums.ContractStatus;
import com.nldv.rentalroom.enums.RoomStatus;
import com.nldv.rentalroom.pojo.Contract;
import com.nldv.rentalroom.repository.ContractRepository;
import com.nldv.rentalroom.repository.RoomRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractExpirationService {

    private final ContractRepository contractRepository;
    private final RoomRepository roomRepository;
    private final NotificationService notificationService;

    public ContractExpirationService(
            ContractRepository contractRepository,
            RoomRepository roomRepository,
            NotificationService notificationService) {

        this.contractRepository = contractRepository;
        this.roomRepository = roomRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(
            cron = "0 0 0 * * *",
            zone = "Asia/Ho_Chi_Minh"
    )
    @Transactional
    public void expireContracts() {

        LocalDate today
                = LocalDate.now();

        List<Contract> contracts
                = contractRepository
                        .findByStatusAndEndDateBefore(
                                ContractStatus.ACTIVE,
                                today
                        );

        for (Contract contract : contracts) {

            contract.setStatus(
                    ContractStatus.EXPIRED);

            if (contract.getRoom() != null) {

                contract.getRoom().setStatus(
                        RoomStatus.AVAILABLE);

                roomRepository.save(
                        contract.getRoom());
            }

            contractRepository.save(contract);

            if (contract.getUser() != null) {

                notificationService
                        .createNotification(
                                contract.getUser().getId(),
                                "Hợp đồng đã hết hạn",
                                "Hợp đồng thuê phòng "
                                + contract.getRoom()
                                        .getRoomNumber()
                                + " đã hết hạn.",
                                "CONTRACT"
                        );
            }
        }
    }
}
