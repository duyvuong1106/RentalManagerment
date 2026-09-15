/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.AreaRequest;
import com.nldv.rentalroom.dto.AreaResponse;
import com.nldv.rentalroom.pojo.Area;
import com.nldv.rentalroom.service.AreaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping
    public ResponseEntity<List<AreaResponse>> findAll() {

        List<AreaResponse> result
                = areaService.findAll()
                        .stream()
                        .map(AreaResponse::fromEntity)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaResponse> findById(
            @PathVariable Integer id) {

        Area area = areaService.findById(id);

        if (area == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                AreaResponse.fromEntity(area)
        );
    }

    @PostMapping
    public ResponseEntity<AreaResponse> create(
            @Valid @RequestBody AreaRequest request) {

        Area area = new Area();

        area.setName(request.getName());
        area.setDescription(request.getDescription());
        area.setStatus(request.getStatus());

        Area saved = areaService.save(area);

        return ResponseEntity.ok(
                AreaResponse.fromEntity(saved)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody AreaRequest request) {

        Area area = areaService.findById(id);

        if (area == null) {
            return ResponseEntity.notFound().build();
        }

        area.setName(request.getName());
        area.setDescription(request.getDescription());
        area.setStatus(request.getStatus());

        Area saved = areaService.save(area);

        return ResponseEntity.ok(
                AreaResponse.fromEntity(saved)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Integer id) {

        Area area = areaService.findById(id);

        if (area == null) {
            return ResponseEntity.notFound().build();
        }

        areaService.deleteById(id);

        return ResponseEntity.ok(
                "Xóa khu vực thành công"
        );
    }
}
