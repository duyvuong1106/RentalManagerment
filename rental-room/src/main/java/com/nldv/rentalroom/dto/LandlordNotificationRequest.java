package com.nldv.rentalroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LandlordNotificationRequest {
    @NotNull private Integer customerId;
    @NotBlank @Size(max=200) private String title;
    @NotBlank @Size(max=1000) private String content;
    @Size(max=50) private String type;
    public LandlordNotificationRequest() {}
    public Integer getCustomerId(){return customerId;} public void setCustomerId(Integer v){customerId=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getContent(){return content;} public void setContent(String v){content=v;}
    public String getType(){return type;} public void setType(String v){type=v;}
}
