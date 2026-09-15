/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.dto.ContractResponse;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.service.ContractService;
import com.nldv.rentalroom.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/contracts")
public class CustomerContractController {

    private final ContractService contractService;
    private final UserService userService;

    public CustomerContractController(
            ContractService contractService,
            UserService userService) {

        this.contractService = contractService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ContractResponse>> getMyContracts(
            Authentication authentication) {

        User customer =
                userService.findByUsername(
                        authentication.getName());

        return ResponseEntity.ok(
                contractService.getCustomerContracts(
                        customer.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractResponse> getContract(
            @PathVariable Integer id,
            Authentication authentication) {

        User customer =
                userService.findByUsername(
                        authentication.getName());

        return ResponseEntity.ok(
                contractService.getCustomerContract(
                        customer.getId(),
                        id));
    }
}
