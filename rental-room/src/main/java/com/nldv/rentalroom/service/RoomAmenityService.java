/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.service;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.pojo.Amenity;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.pojo.RoomAmenity;
import com.nldv.rentalroom.repository.AmenityRepository;
import com.nldv.rentalroom.repository.RoomAmenityRepository;
import com.nldv.rentalroom.repository.RoomRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomAmenityService {

    private final RoomAmenityRepository roomAmenityRepository;
    private final RoomRepository roomRepository;
    private final AmenityRepository amenityRepository;

    public RoomAmenityService(
            RoomAmenityRepository roomAmenityRepository,
            RoomRepository roomRepository,
            AmenityRepository amenityRepository) {

        this.roomAmenityRepository = roomAmenityRepository;
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
    }

    
    public List<RoomAmenity> findByRoomId(Integer roomId) {

        if (!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy phòng");
        }

        return roomAmenityRepository.findByRoomId(roomId);
    }

    
    @Transactional
    public RoomAmenity addAmenity(
            Integer roomId,
            Integer amenityId) {

        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy phòng"));

        Amenity amenity = amenityRepository
                .findById(amenityId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy tiện ích"));

        boolean exists
                = roomAmenityRepository
                        .existsByRoomIdAndAmenityId(
                                roomId,
                                amenityId);

        if (exists) {
            throw new IllegalArgumentException(
                    "Tiện ích đã được thêm vào phòng");
        }

        RoomAmenity roomAmenity
                = new RoomAmenity();

        roomAmenity.setRoomId(roomId);
        roomAmenity.setAmenityId(amenityId);

        roomAmenity.setRoom(room);
        roomAmenity.setAmenity(amenity);

        return roomAmenityRepository.save(
                roomAmenity);
    }

   
    @Transactional
    public void removeAmenity(
            Integer roomId,
            Integer amenityId) {

        if (!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy phòng");
        }

        if (!amenityRepository.existsById(amenityId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy tiện ích");
        }

        boolean exists
                = roomAmenityRepository
                        .existsByRoomIdAndAmenityId(
                                roomId,
                                amenityId);

        if (!exists) {
            throw new IllegalArgumentException(
                    "Tiện ích chưa được thêm vào phòng");
        }

        roomAmenityRepository
                .deleteByRoomIdAndAmenityId(
                        roomId,
                        amenityId);
    }

    
    @Transactional
    public void removeAllByRoomId(
            Integer roomId) {

        roomAmenityRepository
                .deleteByRoomId(roomId);
    }
}
