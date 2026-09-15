package com.nldv.rentalroom.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class ProfileUpdateRequest {
    @Size(max=100) private String firstName;
    @Size(max=100) private String lastName;
    @Email @Size(max=255) private String email;
    @Size(max=20) private String phone;
    @Size(max=255) private String address;
    public ProfileUpdateRequest() {}
    public String getFirstName(){return firstName;} public void setFirstName(String v){firstName=v;}
    public String getLastName(){return lastName;} public void setLastName(String v){lastName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
}
