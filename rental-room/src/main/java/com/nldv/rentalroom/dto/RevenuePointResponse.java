package com.nldv.rentalroom.dto;

public class RevenuePointResponse {
    private String period;
    private long revenue;
    public RevenuePointResponse() {}
    public RevenuePointResponse(String period, long revenue) { this.period=period; this.revenue=revenue; }
    public String getPeriod(){return period;} public void setPeriod(String period){this.period=period;}
    public long getRevenue(){return revenue;} public void setRevenue(long revenue){this.revenue=revenue;}
}
