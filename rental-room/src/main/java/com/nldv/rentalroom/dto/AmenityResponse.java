/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.pojo.Amenity;
import java.time.LocalDateTime;

public class AmenityResponse {

    private Integer id;
    private String name;
    private String description;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public AmenityResponse() {
    }

    public static AmenityResponse fromEntity(Amenity amenity) {

        AmenityResponse response = new AmenityResponse();

        response.id = amenity.getId();
        response.name = amenity.getName();
        response.description = amenity.getDescription();
        response.status = amenity.getStatus();
        response.createdDate = amenity.getCreatedDate();
        response.updatedDate = amenity.getUpdatedDate();

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
