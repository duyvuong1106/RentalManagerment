import api from "./api";

const catalogService = {
  async getAreas() {
    const response = await api.get("/api/areas");
    return response.data;
  },
  async getRoomTypes() {
    const response = await api.get("/api/room-types");
    return response.data;
  },
  async getAmenities() {
    const response = await api.get("/api/amenities");
    return response.data;
  },
  async getServices() {
    const response = await api.get("/api/services");
    return response.data;
  },
};

export default catalogService;
