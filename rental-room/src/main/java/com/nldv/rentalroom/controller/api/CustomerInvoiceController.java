/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.controller.api;

/**
 *
 * @author ASUS
 */


import com.nldv.rentalroom.dto.InvoiceResponse;
import com.nldv.rentalroom.pojo.CustomUserDetails;
import com.nldv.rentalroom.service.InvoiceService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/invoices")
public class CustomerInvoiceController {

    private final InvoiceService invoiceService;

    public CustomerInvoiceController(
            InvoiceService invoiceService) {

        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getInvoices(
            Authentication authentication) {

        Integer customerId =
                getUserId(authentication);

        return ResponseEntity.ok(
                invoiceService.getCustomerInvoices(
                        customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoice(
            @PathVariable Integer id,
            Authentication authentication) {

        Integer customerId =
                getUserId(authentication);

        return ResponseEntity.ok(
                invoiceService.getCustomerInvoice(
                        customerId,
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
