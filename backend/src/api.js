import axios from 'axios';

// Create an axios instance with the base URL of your Java Backend
const api = axios.create({
    baseURL: 'http://localhost:8080/api/users',
});

export default api;
