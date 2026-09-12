package com.nldv.rentalroom.service;

import com.nldv.rentalroom.pojo.RoomType;
import com.nldv.rentalroom.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    public List<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }
}