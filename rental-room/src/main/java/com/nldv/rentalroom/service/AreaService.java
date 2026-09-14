package com.nldv.rentalroom.service;

import com.nldv.rentalroom.pojo.Area;
import com.nldv.rentalroom.repository.AreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public List<Area> findAll() {
        return areaRepository.findAll();
    }

    public Area findById(Integer id) {
        return areaRepository.findById(id).orElse(null);
    }

    public Area save(Area area) {
        return areaRepository.save(area);
    }

    public void deleteById(Integer id) {
        areaRepository.deleteById(id);
    }
}
