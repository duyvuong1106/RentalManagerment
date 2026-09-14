/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AreaRequest {

    @NotBlank(message = "Tên khu vực không được để trống")
    @Size(max = 100, message = "Tên khu vực tối đa 100 ký tự")
    private String name;

    public AreaRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
