package com.nldv.rentalroom.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Collection;

@Entity
@Table(name = "amenities")
public class Amenity extends BaseEntity {

    @NotBlank
    @Size(max = 150)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "amenity")
    private Collection<RoomAmenity> roomAmenities;

    public Amenity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<RoomAmenity> getRoomAmenities() {
        return roomAmenities;
    }

    public void setRoomAmenities(Collection<RoomAmenity> roomAmenities) {
        this.roomAmenities = roomAmenities;
    }

    @Override
    public String toString() {
        return "Amenity{" + "id=" + id + ", name=" + name + '}';
    }
}