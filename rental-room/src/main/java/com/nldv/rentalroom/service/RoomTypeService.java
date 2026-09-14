package com.nldv.rentalroom.service;

import com.nldv.rentalroom.pojo.RoomType;
import com.nldv.rentalroom.repository.RoomTypeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    public List<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }

    public RoomType findById(Integer id) {
        return roomTypeRepository
                .findById(id)
                .orElse(null);
    }

    public RoomType save(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    public void deleteById(Integer id) {
        roomTypeRepository.deleteById(id);
    }
}