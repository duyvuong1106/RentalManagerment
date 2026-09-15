/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */

import com.nldv.rentalroom.dto.InvoiceCreateRequest;
import com.nldv.rentalroom.dto.InvoiceResponse;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.InvoiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/landlord/invoices")
public class LandlordInvoiceController {

    private final InvoiceService invoiceService;

    public LandlordInvoiceController(
            InvoiceService invoiceService) {

        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(
            @Valid @RequestBody InvoiceCreateRequest request,
            Authentication authentication) {

        Integer landlordId =
                getUserId(authentication);

        InvoiceResponse response =
                invoiceService.createInvoice(
                        landlordId,
                        request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getInvoices(
            Authentication authentication) {

        Integer landlordId =
                getUserId(authentication);

        return ResponseEntity.ok(
                invoiceService.getLandlordInvoices(
                        landlordId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoice(
            @PathVariable Integer id,
            Authentication authentication) {

        Integer landlordId =
                getUserId(authentication);

        return ResponseEntity.ok(
                invoiceService.getLandlordInvoice(
                        landlordId,
                        id));
    }

    private Integer getUserId(
            Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication
                        .getPrincipal();

        return userDetails.getUser().getId();
    }
}