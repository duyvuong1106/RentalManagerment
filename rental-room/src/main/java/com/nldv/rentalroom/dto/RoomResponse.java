/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.pojo.Room;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RoomResponse {

    private Integer id;
    private String roomNumber;
    private String title;
    private String description;
    private String address;
    private BigDecimal areaSize;
    private Integer price;
    private String status;

    private Integer areaId;
    private String areaName;

    private Integer roomTypeId;
    private String roomTypeName;

    private Integer landlordId;
    private String landlordUsername;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public RoomResponse() {
    }

    public static RoomResponse fromEntity(Room room) {

        RoomResponse response = new RoomResponse();

        response.id = room.getId();
        response.roomNumber = room.getRoomNumber();
        response.title = room.getTitle();
        response.description = room.getDescription();
        response.address = room.getAddress();
        response.areaSize = room.getAreaSize();
        response.price = room.getPrice();

        if (room.getStatus() != null) {
            response.status = room.getStatus().name();
        }

        if (room.getArea() != null) {
            response.areaId = room.getArea().getId();
            response.areaName = room.getArea().getName();
        }

        if (room.getRoomType() != null) {
            response.roomTypeId = room.getRoomType().getId();
            response.roomTypeName = room.getRoomType().getName();
        }

        if (room.getLandlord() != null) {
            response.landlordId = room.getLandlord().getId();
            response.landlordUsername = room.getLandlord().getUsername();
        }

        response.createdDate = room.getCreatedDate();
        response.updatedDate = room.getUpdatedDate();

        return response;
    }

    public Integer getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getAreaSize() {
        return areaSize;
    }

    public Integer getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public Integer getLandlordId() {
        return landlordId;
    }

    public String getLandlordUsername() {
        return landlordUsername;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
}