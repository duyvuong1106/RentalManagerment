package com.nldv.rentalroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RentalRoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalRoomApplication.class, args);
    }
}