/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */


import com.nldv.rentalroom.pojo.RoomAmenity;

public class RoomAmenityResponse {

    private Integer roomId;
    private Integer amenityId;
    private String amenityName;
    private String amenityDescription;

    public RoomAmenityResponse() {
    }

    public static RoomAmenityResponse fromEntity(
            RoomAmenity roomAmenity) {

        RoomAmenityResponse response =
                new RoomAmenityResponse();

        response.roomId =
                roomAmenity.getRoomId();

        response.amenityId =
                roomAmenity.getAmenityId();

        if (roomAmenity.getAmenity() != null) {

            response.amenityName =
                    roomAmenity.getAmenity().getName();

            response.amenityDescription =
                    roomAmenity.getAmenity().getDescription();
        }

        return response;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public Integer getAmenityId() {
        return amenityId;
    }

    public String getAmenityName() {
        return amenityName;
    }

    public String getAmenityDescription() {
        return amenityDescription;
    }
}
