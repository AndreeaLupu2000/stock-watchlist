import axios from "axios";

//API URL for the users
const API_URL = import.meta.env.VITE_BACKEND_API + "/users";

/**
 * Register a new user
 * @param username - The username of the user
 * @param password - The password of the user
 * @returns {Promise<AxiosResponse<UserDto>>}
 */
export const register = async (username: string, password: string) => {
    console.log(username, password);
    const response = await axios.post(`${API_URL}/register`, { username, password });
    return response;
}

/**
 * Login a user
 * @param username - The username of the user
 * @param password - The password of the user
 * @returns {Promise<AxiosResponse<UserDto>>}
 */
export const login = async (username: string, password: string) => {
    const response = await axios.post(`${API_URL}/login`, { username, password });
    return response;
}

/**
 * Delete a user
 * @param username - The username of the user
 * @returns {Promise<AxiosResponse<UserDto>>}
 */
export const deleteUser = async (username: string) => {
    const response = await axios.delete(`${API_URL}/${username}`);
    return response.data;
}
