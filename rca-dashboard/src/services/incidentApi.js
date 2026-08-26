import apiClient from "./apiClient";

export const getIncidents = async () => {
  const response = await apiClient.get("/api/incidents");

  const data = response.data;

  // Backend directly array return kare
  if (Array.isArray(data)) {
    return data;
  }

  // Future mein backend wrapper use kare
  if (Array.isArray(data.content)) {
    return data.content;
  }

  if (Array.isArray(data.incidents)) {
    return data.incidents;
  }

  // Unexpected response
  console.error("Unexpected incidents API response:", data);

  return [];
};

export const getIncident = async (id) => {
  const response = await apiClient.get(`/api/incidents/${id}`);

  return response.data;
};