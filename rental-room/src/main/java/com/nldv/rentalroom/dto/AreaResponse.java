/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.pojo.Area;

public class AreaResponse {

    private Integer id;
    private String name;

    public AreaResponse() {
    }

    public static AreaResponse fromEntity(Area area) {

        AreaResponse response = new AreaResponse();

        response.id = area.getId();
        response.name = area.getName();

        return response;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
