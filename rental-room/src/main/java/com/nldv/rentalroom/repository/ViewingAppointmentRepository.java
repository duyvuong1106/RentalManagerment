package com.nldv.rentalroom.repository;

import com.nldv.rentalroom.enums.ViewingStatus;
import com.nldv.rentalroom.pojo.ViewingAppointment;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewingAppointmentRepository extends JpaRepository<ViewingAppointment, Integer> {
    List<ViewingAppointment> findByCustomerIdOrderByAppointmentDateAscAppointmentTimeAsc(Integer customerId);
    List<ViewingAppointment> findByLandlordIdOrderByAppointmentDateAscAppointmentTimeAsc(Integer landlordId);
    boolean existsByRoomIdAndAppointmentDateAndAppointmentTimeAndStatusIn(
            Integer roomId, LocalDate date, LocalTime time, List<ViewingStatus> statuses);
}
