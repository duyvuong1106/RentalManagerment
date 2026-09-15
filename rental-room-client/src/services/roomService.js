import api from "./api";

const roomService = {
  async getRooms(filters = {}) {
    const params = {};
    Object.entries(filters).forEach(([key, value]) => {
      if (value !== "" && value !== null && value !== undefined)
        params[key] = value;
    });
    const response = await api.get("/api/rooms", { params });
    return response.data;
  },
  async getRoomById(roomId) {
    const response = await api.get(`/api/rooms/${roomId}`);
    return response.data;
  },
  async getRoomImages(roomId) {
    const response = await api.get(`/api/rooms/${roomId}/images`);
    return response.data;
  },
  async getRoomAmenities(roomId) {
    const response = await api.get(`/api/rooms/${roomId}/amenities`);
    return response.data;
  },
  async getRoomReviews(roomId) {
    const response = await api.get(`/api/rooms/${roomId}/reviews`);
    return response.data;
  },
  async createRoom(data) {
    const response = await api.post("/api/landlord/rooms", data);
    return response.data;
  },
  async getLandlordRooms() {
    const response = await api.get("/api/landlord/rooms");
    return response.data;
  },
  async getLandlordRoom(roomId) {
    const response = await api.get(`/api/landlord/rooms/${roomId}`);
    return response.data;
  },
  async updateRoom(roomId, data) {
    const response = await api.put(`/api/landlord/rooms/${roomId}`, data);
    return response.data;
  },
  async deleteRoom(roomId) {
    const response = await api.delete(`/api/landlord/rooms/${roomId}`);
    return response.data;
  },
  async uploadImage(roomId, file, thumbnail = false) {
    const form = new FormData();
    form.append("file", file);
    form.append("thumbnail", thumbnail ? "true" : "false");
    const response = await api.post(
      `/api/landlord/rooms/${roomId}/images`,
      form,
      {
        headers: { "Content-Type": "multipart/form-data" },
      },
    );
    return response.data;
  },
  async deleteImage(roomId, imageId) {
    const response = await api.delete(
      `/api/landlord/rooms/${roomId}/images/${imageId}`,
    );
    return response.data;
  },
  async setThumbnail(roomId, imageId) {
    const response = await api.put(
      `/api/landlord/rooms/${roomId}/images/${imageId}/thumbnail`,
    );
    return response.data;
  },
  async addAmenity(roomId, amenityId) {
    const response = await api.post(
      `/api/landlord/rooms/${roomId}/amenities/${amenityId}`,
    );
    return response.data;
  },
  async removeAmenity(roomId, amenityId) {
    const response = await api.delete(
      `/api/landlord/rooms/${roomId}/amenities/${amenityId}`,
    );
    return response.data;
  },
};

export default roomService;
