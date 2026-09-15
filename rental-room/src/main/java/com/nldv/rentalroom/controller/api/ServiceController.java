/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.dto.ServiceRequest;
import com.nldv.rentalroom.dto.ServiceResponse;
import com.nldv.rentalroom.pojo.Service;
import com.nldv.rentalroom.service.ServiceService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> findAll() {

        List<ServiceResponse> result =
                serviceService.findAll()
                        .stream()
                        .map(ServiceResponse::fromEntity)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> findById(
            @PathVariable Integer id) {

        Service service = serviceService.findById(id);

        if (service == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                ServiceResponse.fromEntity(service)
        );
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> create(
            @Valid @RequestBody ServiceRequest request) {

        Service service = new Service();

        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setStatus(request.getStatus());
        service.setUnit(request.getUnit());

        Service saved = serviceService.save(service);

        return ResponseEntity.ok(
                ServiceResponse.fromEntity(saved)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody ServiceRequest request) {

        Service service = serviceService.findById(id);

        if (service == null) {
            return ResponseEntity.notFound().build();
        }

        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setStatus(request.getStatus());
        service.setUnit(request.getUnit());

        Service saved = serviceService.save(service);

        return ResponseEntity.ok(
                ServiceResponse.fromEntity(saved)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Integer id) {

        Service service = serviceService.findById(id);

        if (service == null) {
            return ResponseEntity.notFound().build();
        }

        serviceService.deleteById(id);

        return ResponseEntity.ok(
                "Xóa dịch vụ thành công"
        );
    }
}