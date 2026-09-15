package com.nldv.rentalroom.dto;

import com.nldv.rentalroom.pojo.ViewingAppointment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ViewingAppointmentResponse {
    private Integer id;
    private Integer customerId;
    private String customerUsername;
    private Integer landlordId;
    private String landlordUsername;
    private Integer roomId;
    private String roomNumber;
    private String roomTitle;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String message;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static ViewingAppointmentResponse fromEntity(ViewingAppointment a) {
        ViewingAppointmentResponse r = new ViewingAppointmentResponse();
        r.id = a.getId();
        if (a.getCustomer() != null) { r.customerId = a.getCustomer().getId(); r.customerUsername = a.getCustomer().getUsername(); }
        if (a.getLandlord() != null) { r.landlordId = a.getLandlord().getId(); r.landlordUsername = a.getLandlord().getUsername(); }
        if (a.getRoom() != null) { r.roomId = a.getRoom().getId(); r.roomNumber = a.getRoom().getRoomNumber(); r.roomTitle = a.getRoom().getTitle(); }
        r.appointmentDate = a.getAppointmentDate();
        r.appointmentTime = a.getAppointmentTime();
        r.message = a.getMessage();
        r.status = a.getStatus() == null ? null : a.getStatus().name();
        r.createdDate = a.getCreatedDate();
        r.updatedDate = a.getUpdatedDate();
        return r;
    }
    public Integer getId(){return id;} public Integer getCustomerId(){return customerId;} public String getCustomerUsername(){return customerUsername;}
    public Integer getLandlordId(){return landlordId;} public String getLandlordUsername(){return landlordUsername;} public Integer getRoomId(){return roomId;}
    public String getRoomNumber(){return roomNumber;} public String getRoomTitle(){return roomTitle;} public LocalDate getAppointmentDate(){return appointmentDate;}
    public LocalTime getAppointmentTime(){return appointmentTime;} public String getMessage(){return message;} public String getStatus(){return status;}
    public LocalDateTime getCreatedDate(){return createdDate;} public LocalDateTime getUpdatedDate(){return updatedDate;}
}
