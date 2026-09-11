package com.nldv.rentalroom.pojo;

import com.nldv.rentalroom.enums.RoomStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Table(name = "rooms")
public class Room extends BaseEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @Size(max = 200)
    @Column(name = "title")
    private String title;

    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Column(name = "address")
    private String address;

    @Column(name = "area_size", precision = 10, scale = 2)
    private BigDecimal areaSize;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RoomStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @OneToMany(mappedBy = "room")
    private Collection<RoomImage> roomImages;

    @OneToMany(mappedBy = "room")
    private Collection<RoomAmenity> roomAmenities;

    @OneToMany(mappedBy = "room")
    private Collection<RentalRequest> rentalRequests;

    @OneToMany(mappedBy = "room")
    private Collection<Contract> contracts;

    public Room() {
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(BigDecimal areaSize) {
        this.areaSize = areaSize;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Collection<RoomImage> getRoomImages() {
        return roomImages;
    }

    public void setRoomImages(Collection<RoomImage> roomImages) {
        this.roomImages = roomImages;
    }

    public Collection<RoomAmenity> getRoomAmenities() {
        return roomAmenities;
    }

    public void setRoomAmenities(Collection<RoomAmenity> roomAmenities) {
        this.roomAmenities = roomAmenities;
    }

    public Collection<RentalRequest> getRentalRequests() {
        return rentalRequests;
    }

    public void setRentalRequests(Collection<RentalRequest> rentalRequests) {
        this.rentalRequests = rentalRequests;
    }

    public Collection<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(Collection<Contract> contracts) {
        this.contracts = contracts;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", roomNumber=" + roomNumber + '}';
    }
}