/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */


import com.nldv.rentalroom.enums.UserStatus;
import jakarta.validation.constraints.NotNull;

public class UserStatusRequest {

    @NotNull(message = "Status không được để trống")
    private UserStatus status;

    public UserStatusRequest() {
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}