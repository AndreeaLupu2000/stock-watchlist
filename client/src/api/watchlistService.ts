import axios from "axios";

//API URL for the watchlist
const API_URL = import.meta.env.VITE_BACKEND_API + "/watchlist";

/**
 * Get the watchlist for a user
 * @param username - The username of the user
 * @returns {Promise<AxiosResponse<WatchlistDto[]>>}
 */
export const getWatchlist = async (username: string) => {
    const response = await axios.get(`${API_URL}/${username}`);
    return response;
}

/**
 * Add a stock to the watchlist
 * @param symbol - The symbol of the stock
 * @param username - The username of the user
 * @returns {Promise<AxiosResponse<WatchlistDto>>}
 */
export const addStockToWatchlist = async (symbol: string, username: string) => {  
    const response = await axios.post(`${API_URL}/${symbol}`, { username });
    return response;
}

/**
 * Remove a stock from the watchlist
 * @param symbol - The symbol of the stock
 * @param username - The username of the user
 * @returns {Promise<AxiosResponse<WatchlistDto>>}
 */
export const removeStockFromWatchlist = async (symbol: string, username: string) => {
    const response = await axios.delete(`${API_URL}/${symbol}`, {  data: { username } });
    return response;
}