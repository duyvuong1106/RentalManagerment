/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */


import com.nldv.rentalroom.pojo.RoomImage;
import java.time.LocalDateTime;

public class RoomImageResponse {

    private Integer id;
    private Integer roomId;
    private String imageUrl;
    private String publicId;
    private Boolean isThumbnail;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public RoomImageResponse() {
    }

    public static RoomImageResponse fromEntity(RoomImage image) {

        RoomImageResponse response = new RoomImageResponse();

        response.id = image.getId();

        if (image.getRoom() != null) {
            response.roomId = image.getRoom().getId();
        }

        response.imageUrl = image.getImageUrl();
        response.publicId = image.getPublicId();
        response.isThumbnail = image.getIsThumbnail();
        response.createdDate = image.getCreatedDate();
        response.updatedDate = image.getUpdatedDate();

        return response;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public Boolean getIsThumbnail() {
        return isThumbnail;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
}