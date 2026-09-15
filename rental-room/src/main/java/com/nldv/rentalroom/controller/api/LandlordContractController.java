/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.ContractCreateRequest;
import com.nldv.rentalroom.dto.ContractResponse;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.service.ContractService;
import com.nldv.rentalroom.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/landlord/contracts")
public class LandlordContractController {

    private final ContractService contractService;
    private final UserService userService;

    public LandlordContractController(
            ContractService contractService,
            UserService userService) {

        this.contractService = contractService;
        this.userService = userService;
    }

    
    @PostMapping
    public ResponseEntity<?> createContract(
            @Valid @RequestBody ContractCreateRequest request,
            Authentication authentication) {

        User landlord
                = userService.findByUsername(
                        authentication.getName());

        try {

            ContractResponse response
                    = contractService.createContract(
                            landlord.getId(),
                            request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    
    @GetMapping
    public ResponseEntity<List<ContractResponse>>
            getContracts(
                    Authentication authentication) {

        User landlord
                = userService.findByUsername(
                        authentication.getName());

        return ResponseEntity.ok(
                contractService.getLandlordContracts(
                        landlord.getId()));
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<?> getContract(
            @PathVariable Integer id,
            Authentication authentication) {

        User landlord
                = userService.findByUsername(
                        authentication.getName());

        try {

            return ResponseEntity.ok(
                    contractService.getLandlordContract(
                            landlord.getId(),
                            id));

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateContract(
            @PathVariable Integer id,
            Authentication authentication) {

        User landlord
                = userService.findByUsername(
                        authentication.getName());

        try {

            return ResponseEntity.ok(
                    contractService.activateContract(
                            landlord.getId(),
                            id));

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

   
    @PutMapping("/{id}/terminate")
    public ResponseEntity<?> terminateContract(
            @PathVariable Integer id,
            Authentication authentication) {

        User landlord
                = userService.findByUsername(
                        authentication.getName());

        try {

            return ResponseEntity.ok(
                    contractService.terminateContract(
                            landlord.getId(),
                            id));

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
