package com.nldv.rentalroom.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_amenities")
@IdClass(RoomAmenity.RoomAmenityId.class)
public class RoomAmenity {

    @Id
    @Column(name = "room_id")
    private Integer roomId;

    @Id
    @Column(name = "amenity_id")
    private Integer amenityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "room_id",
            insertable = false,
            updatable = false
    )
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "amenity_id",
            insertable = false,
            updatable = false
    )
    private Amenity amenity;

    public RoomAmenity() {
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(Integer amenityId) {
        this.amenityId = amenityId;
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

    public static class RoomAmenityId {

        private Integer roomId;
        private Integer amenityId;

        public RoomAmenityId() {
        }

        public RoomAmenityId(
                Integer roomId,
                Integer amenityId) {

            this.roomId = roomId;
            this.amenityId = amenityId;
        }

        public Integer getRoomId() {
            return roomId;
        }

        public void setRoomId(Integer roomId) {
            this.roomId = roomId;
        }

        public Integer getAmenityId() {
            return amenityId;
        }

        public void setAmenityId(Integer amenityId) {
            this.amenityId = amenityId;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) {
                return true;
            }

            if (!(o instanceof RoomAmenityId)) {
                return false;
            }

            RoomAmenityId that =
                    (RoomAmenityId) o;

            return roomId.equals(that.roomId)
                    && amenityId.equals(that.amenityId);
        }

        @Override
        public int hashCode() {
            return 31 * roomId.hashCode()
                    + amenityId.hashCode();
        }
    }
}