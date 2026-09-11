package com.nldv.rentalroom.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "room_amenities")
@IdClass(RoomAmenity.RoomAmenityId.class)
public class RoomAmenity implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;

    public RoomAmenity() {
    }

    public RoomAmenity(Room room, Amenity amenity) {
        this.room = room;
        this.amenity = amenity;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Amenity getAmenity() {
        return amenity;
    }

    public void setAmenity(Amenity amenity) {
        this.amenity = amenity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (room != null ? room.hashCode() : 0);
        hash += (amenity != null ? amenity.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RoomAmenity)) {
            return false;
        }

        RoomAmenity other = (RoomAmenity) object;

        if ((this.room == null && other.room != null)
                || (this.room != null && !this.room.equals(other.room))) {
            return false;
        }

        if ((this.amenity == null && other.amenity != null)
                || (this.amenity != null && !this.amenity.equals(other.amenity))) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "com.nldv.rentalroom.pojo.RoomAmenity[ room="
                + room + ", amenity=" + amenity + " ]";
    }

    /**
     * Composite primary key
     */
    public static class RoomAmenityId implements Serializable {

        private Integer room;
        private Integer amenity;

        public RoomAmenityId() {
        }

        public RoomAmenityId(Integer room, Integer amenity) {
            this.room = room;
            this.amenity = amenity;
        }

        public Integer getRoom() {
            return room;
        }

        public void setRoom(Integer room) {
            this.room = room;
        }

        public Integer getAmenity() {
            return amenity;
        }

        public void setAmenity(Integer amenity) {
            this.amenity = amenity;
        }

        @Override
        public int hashCode() {
            int hash = 0;
            hash += (room != null ? room.hashCode() : 0);
            hash += (amenity != null ? amenity.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof RoomAmenityId)) {
                return false;
            }

            RoomAmenityId other = (RoomAmenityId) object;

            if ((this.room == null && other.room != null)
                    || (this.room != null && !this.room.equals(other.room))) {
                return false;
            }

            if ((this.amenity == null && other.amenity != null)
                    || (this.amenity != null && !this.amenity.equals(other.amenity))) {
                return false;
            }

            return true;
        }
    }
}