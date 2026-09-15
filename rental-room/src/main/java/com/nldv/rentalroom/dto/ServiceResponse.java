/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.pojo.Service;
import java.time.LocalDateTime;

public class ServiceResponse {

    private Integer id;
    private String name;
    private String description;
    private String status;
    private String unit;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public ServiceResponse() {
    }

    public static ServiceResponse fromEntity(Service service) {

        ServiceResponse response = new ServiceResponse();

        response.id = service.getId();
        response.name = service.getName();
        response.description = service.getDescription();
        response.status = service.getStatus();
        response.unit = service.getUnit();
        response.createdDate = service.getCreatedDate();
        response.updatedDate = service.getUpdatedDate();

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

    public String getUnit() {
        return unit;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
}
