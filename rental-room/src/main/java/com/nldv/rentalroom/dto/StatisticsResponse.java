package com.nldv.rentalroom.dto;

public class StatisticsResponse {
    private long totalRooms;
    private long availableRooms;
    private long pendingRooms;
    private long rentedRooms;
    private long totalCustomers;
    private long pendingRentalRequests;
    private long activeContracts;
    private long expiringContracts;
    private long totalInvoices;
    private long totalPayments;
    private long successfulPayments;
    private long pendingPayments;
    private long totalRevenue;
    private long paidAmount;
    private long unpaidAmount;
    private double occupancyRate;
    private double averageRating;
    private long totalReviews;

    public long getTotalRooms(){return totalRooms;} public void setTotalRooms(long v){totalRooms=v;}
    public long getAvailableRooms(){return availableRooms;} public void setAvailableRooms(long v){availableRooms=v;}
    public long getPendingRooms(){return pendingRooms;} public void setPendingRooms(long v){pendingRooms=v;}
    public long getRentedRooms(){return rentedRooms;} public void setRentedRooms(long v){rentedRooms=v;}
    public long getTotalCustomers(){return totalCustomers;} public void setTotalCustomers(long v){totalCustomers=v;}
    public long getPendingRentalRequests(){return pendingRentalRequests;} public void setPendingRentalRequests(long v){pendingRentalRequests=v;}
    public long getActiveContracts(){return activeContracts;} public void setActiveContracts(long v){activeContracts=v;}
    public long getExpiringContracts(){return expiringContracts;} public void setExpiringContracts(long v){expiringContracts=v;}
    public long getTotalInvoices(){return totalInvoices;} public void setTotalInvoices(long v){totalInvoices=v;}
    public long getTotalPayments(){return totalPayments;} public void setTotalPayments(long v){totalPayments=v;}
    public long getSuccessfulPayments(){return successfulPayments;} public void setSuccessfulPayments(long v){successfulPayments=v;}
    public long getPendingPayments(){return pendingPayments;} public void setPendingPayments(long v){pendingPayments=v;}
    public long getTotalRevenue(){return totalRevenue;} public void setTotalRevenue(long v){totalRevenue=v;}
    public long getPaidAmount(){return paidAmount;} public void setPaidAmount(long v){paidAmount=v;}
    public long getUnpaidAmount(){return unpaidAmount;} public void setUnpaidAmount(long v){unpaidAmount=v;}
    public double getOccupancyRate(){return occupancyRate;} public void setOccupancyRate(double v){occupancyRate=v;}
    public double getAverageRating(){return averageRating;} public void setAverageRating(double v){averageRating=v;}
    public long getTotalReviews(){return totalReviews;} public void setTotalReviews(long v){totalReviews=v;}
}
