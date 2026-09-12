package com.nldv.rentalroom.service;

import com.nldv.rentalroom.repository.ServiceRepository;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<com.nldv.rentalroom.pojo.Service> findAll() {
        return serviceRepository.findAll();
    }

    public com.nldv.rentalroom.pojo.Service findById(Integer id) {
        return serviceRepository.findById(id).orElse(null);
    }

    public com.nldv.rentalroom.pojo.Service save(
            com.nldv.rentalroom.pojo.Service service) {

        return serviceRepository.save(service);
    }

    public void deleteById(Integer id) {
        serviceRepository.deleteById(id);
    }
}