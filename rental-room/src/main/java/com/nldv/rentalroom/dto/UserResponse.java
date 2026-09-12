/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.enums.UserRole;
import com.nldv.rentalroom.enums.UserStatus;
import com.nldv.rentalroom.pojo.User;
import java.time.LocalDateTime;

public class UserResponse {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private UserRole role;
    private UserStatus status;
    private String avatarUrl;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public UserResponse() {
    }

    public static UserResponse fromUser(User user) {

        UserResponse response = new UserResponse();

        response.id = user.getId();
        response.username = user.getUsername();
        response.firstName = user.getFirstName();
        response.lastName = user.getLastName();
        response.email = user.getEmail();
        response.phone = user.getPhone();
        response.address = user.getAddress();
        response.role = user.getRole();
        response.status = user.getStatus();
        response.avatarUrl = user.getAvatarUrl();
        response.createdDate = user.getCreatedDate();
        response.updatedDate = user.getUpdatedDate();

        return response;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public UserRole getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
}