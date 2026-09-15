import api from "./api";

export async function getLandlordStatistics() {
  const response = await api.get("/api/landlord/statistics");

  return response.data;
}

export async function getLandlordRevenue(year) {
  const response = await api.get("/api/landlord/statistics/revenue", {
    params: {
      year,
    },
  });

  return response.data;
}

export async function getAdminStatistics() {
  const response = await api.get("/api/admin/statistics");

  return response.data;
}

export async function getAdminRevenue(year) {
  const response = await api.get("/api/admin/statistics/revenue", {
    params: {
      year,
    },
  });

  return response.data;
}
