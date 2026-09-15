/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.dto;

/**
 *
 * @author ASUS
 */
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class ContractCreateRequest {

    @NotNull(message = "Rental request ID không được để trống")
    private Integer rentalRequestId;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

    @NotNull(message = "Tiền thuê không được để trống")
    @Min(value = 1, message = "Tiền thuê phải lớn hơn 0")
    private Integer monthlyRent;

    @NotNull(message = "Tiền cọc không được để trống")
    @Min(value = 0, message = "Tiền cọc không được âm")
    private Integer deposit;

    @Size(
            max = 500,
            message = "Đường dẫn hợp đồng không được vượt quá 500 ký tự"
    )
    private String contractUrl;

    public ContractCreateRequest() {
    }

    public Integer getRentalRequestId() {
        return rentalRequestId;
    }

    public void setRentalRequestId(Integer rentalRequestId) {
        this.rentalRequestId = rentalRequestId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(Integer monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }
}
