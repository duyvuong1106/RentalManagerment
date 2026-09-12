package com.nldv.rentalroom.service;

import com.nldv.rentalroom.pojo.Amenity;
import com.nldv.rentalroom.repository.AmenityRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public List<Amenity> findAll() {
        return amenityRepository.findAll();
    }

    public Amenity findById(Integer id) {
        return amenityRepository.findById(id).orElse(null);
    }

    public Amenity save(Amenity amenity) {
        return amenityRepository.save(amenity);
    }

    public void deleteById(Integer id) {
        amenityRepository.deleteById(id);
    }
}