/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller;

import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author ASUS
 */
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
