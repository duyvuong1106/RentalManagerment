/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.pojo.RoomType;
import java.time.LocalDateTime;

public class RoomTypeResponse {

    private Integer id;
    private String name;
    private String description;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public RoomTypeResponse() {
    }

    public static RoomTypeResponse fromEntity(RoomType roomType) {

        RoomTypeResponse response = new RoomTypeResponse();

        response.id = roomType.getId();
        response.name = roomType.getName();
        response.description = roomType.getDescription();
        response.status = roomType.getStatus();
        response.createdDate = roomType.getCreatedDate();
        response.updatedDate = roomType.getUpdatedDate();

        return response;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
}