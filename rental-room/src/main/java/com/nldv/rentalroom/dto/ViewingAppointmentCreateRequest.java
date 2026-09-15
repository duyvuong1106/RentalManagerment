package com.nldv.rentalroom.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

public class ViewingAppointmentCreateRequest {

    @NotNull(message = "Phòng không được để trống")
    private Integer roomId;

    @NotNull(message = "Ngày xem phòng không được để trống")
    @FutureOrPresent(message = "Ngày xem phòng phải từ hôm nay trở đi")
    private LocalDate appointmentDate;

    @NotNull(message = "Giờ xem phòng không được để trống")
    private LocalTime appointmentTime;

    @Size(max = 1000, message = "Nội dung tối đa 1000 ký tự")
    private String message;

    public ViewingAppointmentCreateRequest() {}
    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
