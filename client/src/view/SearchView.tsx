import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getStocks, searchStocks } from "../api/stockService";
import StockCard from "../components/StockCard";
import type { StockDto } from "../dto/StockDto";
import AddToWatchlistModal from "../components/AddToWatchlistModal";
import { addStockToWatchlist, getWatchlist, removeStockFromWatchlist } from "../api/watchlistService";
import type { WatchlistDto } from "../dto/WatchlistDto";

const SearchView = () => {

    // Navigation
    const navigate = useNavigate();

    // State variables
    // Search query by symbol
    const [searchQueryBySymbol, setSearchQueryBySymbol] = useState("");
    // Stocks (search results)
    const [stocks, setStocks] = useState<StockDto[]>([]);
    // Default stocks (all historical stocks)
    const [defaultStocks, setDefaultStocks] = useState<StockDto[]>([]);
    // Watchlist (user's watchlist)
    const [watchlist, setWatchlist] = useState<WatchlistDto[]>([]);

    // State variable for the default view to track if the user is on the default view or the watchlist view
    const [defaultView, setDefaultView] = useState(true);
    // State variable for the stock card selected to track the stock card selected by the user
    const [stockCardSelected, setStockCardSelected] = useState<StockDto | null>(null);
    // State variable for the loading state to track if the stocks are loading
    const [isLoading, setIsLoading] = useState(false);

    /**
     * Fetch the stocks and watchlist when the component mounts
     * @returns void
     */
    useEffect(() => {
        fetchStocks();
        fetchWatchlist();
    }, []);

    /**
     * Set the stocks to the default stocks when the default view is true to display all historical stocks (mock data + searched variables)
     * @returns void
     */
    useEffect(() => {
        if (defaultView) {
            setStocks(defaultStocks);
        }
    }, [defaultStocks, defaultView]);

    /**
     * Fetch the stocks from the server
     * @returns void
     */
    const fetchStocks = async () => {
        const response = await getStocks();
        if (response.status === 200) {
            setDefaultStocks(response.data.filter((stock: StockDto) => stock.price !== 0));
        }
    }

    /**
     * Fetch the watchlist from the server
     * @returns void
     */
    const fetchWatchlist = async () => {

        // Get the username from the local storage
        const username = localStorage.getItem("username");

        // If the username is not found, return
        if (!username) {
            console.log("Username not found");
            return;
        }

        // Fetch the watchlist from the server
        const response = await getWatchlist(username);

        // If the watchlist is found, set the watchlist
        if (response.status === 200) {
            setWatchlist(response.data);
        }
    }

    /**
     * Handle the search for stocks
     * @returns void
     */
    const handleSearch = async () => {

        // Set the loading state to true for the user to see that the stocks are loading
        setIsLoading(true);

        // Fetch the stocks from the server
        const response = await searchStocks(searchQueryBySymbol);

        // If the stocks are found, set the stocks
        if (response.status === 200) {
            setStocks(response.data);
            // Set the default view to false to display the search results
            setDefaultView(false);
            // Set the loading state to false
            setIsLoading(false);
        } else {
            // If the stocks are not found, set the stocks to an empty array
            setStocks([]);
            // Set the loading state to false
            setIsLoading(false);
        }
    }

    /**
     * Handle the addition of a stock to the watchlist
     * @param stock - The stock to add to the watchlist
     * @returns void
     */
    const handleAddToWatchlist = async (stock: StockDto) => {

        // Get the username from the local storage
        const username = localStorage.getItem("username");

        // If the username is not found, return
        if (!username) {
            console.log("Username not found");
            return;
        }

        // Add the stock to the watchlist
        const response = await addStockToWatchlist(stock.symbol, username);

        // If the stock is added to the watchlist, set the watchlist
        if (response.status === 200) {
            // Create a new watchlist item
            const newWatchlistItem: WatchlistDto = {
                id: response.data.id,
                createdAt: response.data.createdAt,
                stock: stock,
                username: username
            };
            // Set the watchlist to the new watchlist item
            setWatchlist([...watchlist, newWatchlistItem]);
        }
    }

    /**
     * Handle the removal of a stock from the watchlist
     * @param stock - The stock to remove from the watchlist
     * @returns void
     */
    const handleRemoveFromWatchlist = async (stock: StockDto) => {

        // Get the username from the local storage
        const username = localStorage.getItem("username");

        // If the username is not found, return
        if (!username) {
            console.log("Username not found");
            return;
        }

        // Remove the stock from the watchlist
        const response = await removeStockFromWatchlist(stock.symbol, username);

        // If the stock is removed from the watchlist in the backend, set the watchlist
        if (response.status === 200) {
            // Set the watchlist to the new watchlist item
            setWatchlist(watchlist.filter(item => item.stock.symbol !== stock.symbol));
            // Set the stocks to the new stocks
            setStocks(stocks.filter(item => item.symbol !== stock.symbol));
        }
    }

    /**
     * Handle the watchlist view
     * @returns void
     */
    const handleWatchlist = async () => {
        // Set the default view to false to display the watchlist
        setDefaultView(false);
        // Set the stocks to the watchlist
        setStocks(watchlist.map(item => item.stock));
    }

    /**
     * Check if a stock is in the watchlist to set the star icon to filled or empty
     * @param stockSymbol - The symbol of the stock to check
     * @returns boolean
     */
    const isStockInWatchlist = (stockSymbol: string) => {
        return watchlist.some(item => item.stock.symbol === stockSymbol);
    }

    return (
        <div className="w-full max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 mt-10 items-center justify-center">
            {/* ------------------ Logout Button ------------------ */}
            <div className="fixed top-4 left-4 z-50">
                <button
                    onClick={() => {
                        // Remove the username from the local storage
                        localStorage.removeItem("username");
                        // Navigate to the login page
                        navigate("/");
                    }}
                    className="text-blue-600 hover:underline text-md font-semibold bg-white px-3 py-1 rounded shadow"
                >
                    Logout
                </button>
            </div>

            {/* ------------------ Search Card ------------------ */}
            <div className="justify-center items-center bg-white p-6 rounded-md shadow-md mb-6">
                <h1 className="text-2xl font-bold mb-6 text-center mb-4">Search Stocks</h1>

                {/* ------------------ Search Input ------------------ */}
                <div className="mb-6 relative items-end flex flex-row gap-4 justify-center">
                    <div className="relative justify-center items-center w-3/4">
                        {/* ------------------ Search Input ------------------ */}
                        <input
                            type="text"
                            placeholder="Search for a stock by symbol or company name"
                            className="w-full p-2 border border-gray-300 rounded-md"
                            value={searchQueryBySymbol}
                            onChange={(e) => setSearchQueryBySymbol(e.target.value)}
                        />
                    </div>

                    {/* ------------------ Search Button ------------------ */}
                    <button
                        onClick={handleSearch}
                        className="bg-blue-500 text-white rounded-md w-1/4 hover:bg-blue-600 p-2"
                    >
                        Search
                    </button>
                </div>
            </div>

            {/* ------------------ Watchlist Button ------------------ */}
            <div className="mb-6 relative justify-center items-center flex flex-row gap-4">

                {/* ------------------ All Historical Stocks Button ------------------ */}
                <button
                    onClick={() => {
                        setDefaultView(true);
                        window.location.reload();
                    }}
                    className="bg-orange-300 text-white hover:underline text-md font-semibold p-3 rounded shadow w-1/2 hover:bg-orange-400"
                >
                    All Historical Stocks
                </button>

                {/* ------------------ Watchlist Button ------------------ */}
                <button
                    onClick={handleWatchlist}
                    className="bg-violet-300 text-white hover:underline text-md font-semibold p-3 rounded shadow w-1/2 hover:bg-violet-400"
                >
                    Watchlist
                </button>
            </div>

            {/* ------------------ Stock Cards ------------------ */}
            {isLoading ? (
                <div className="flex justify-center items-center py-16">
                    <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-500"></div>
                </div>
            ) : (
                <div className="grid grid-cols-2 gap-4">
                    {stocks.map((stock: StockDto) => (
                        <StockCard
                            key={stock.symbol}
                            stock={stock}
                            onAddToWatchlist={() => setStockCardSelected(stock)}
                            isInWatchlist={isStockInWatchlist(stock.symbol)}
                        />
                    ))}
                </div>
            )}

            {stockCardSelected && (
                <AddToWatchlistModal
                    stock={stockCardSelected}
                    onAddToWatchlist={handleAddToWatchlist}
                    onRemoveFromWatchlist={handleRemoveFromWatchlist}
                    onClose={() => setStockCardSelected(null)}
                    isInWatchlist={isStockInWatchlist(stockCardSelected.symbol)}
                />
            )}
        </div>
    )
}

export default SearchView;