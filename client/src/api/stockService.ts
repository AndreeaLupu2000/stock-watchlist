import axios from "axios";

//API URL for the stocks
const API_URL = import.meta.env.VITE_BACKEND_API + "/stocks";

/**
 * Get all stocks
 * @returns {Promise<AxiosResponse<StockDto[]>>}
 */
export const getStocks = async () => {
    const response = await axios.get(API_URL + "/");
    return response;
}

/**
 * Search for stocks
 * @param query - The query to search for
 * @returns {Promise<AxiosResponse<StockDto[]>>}
 */
export const searchStocks = async (query: String) => {
    if (!query) {
        throw new Error("Symbol and company name are required");
    }
    const response = await axios.get(`${API_URL}/search?query=${query}`);
    return response
}