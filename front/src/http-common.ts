import axios, { AxiosInstance } from "axios";

/**
 * The app's AXIOS (http client).
 * The properties are set
 */
const apiClient: AxiosInstance = axios.create({
  baseURL: process.env.VUE_APP_URL,
  headers: {
    "Content-type": "application/json",
    "Authorization": "Bearer " + localStorage.getItem('token')
  },
});

export default apiClient;