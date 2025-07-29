import type { StockDto } from "../dto/StockDto";
import { StarIcon } from "@heroicons/react/24/solid";
import { StarIcon as StarOutlineIcon } from "@heroicons/react/24/outline";
import { TrashIcon } from "@heroicons/react/24/solid";

/**
 * Props for the AddToWatchlistModal component
 */
interface AddToWatchlistModalProps {
    stock: StockDto;
    onAddToWatchlist: (stock: StockDto) => void;
    onRemoveFromWatchlist: (stock: StockDto) => void;
    onClose: () => void;
    isInWatchlist?: boolean;
}

const AddToWatchlistModal = ({ stock, onAddToWatchlist, onRemoveFromWatchlist, onClose, isInWatchlist = false }: AddToWatchlistModalProps) => {

    /**
     * Handle the click of the star icon to add the stock to the watchlist
     * @returns void
     */
    const handleStarClick = () => {
        // Add the stock to the watchlist
        onAddToWatchlist(stock);
        // Reload the page
        window.location.reload();
        // Close the modal
        onClose();
    };

    /**
     * Handle the click of the trash icon to remove the stock from the watchlist
     * @returns void
     */
    const handleRemoveFromWatchlist = () => {
        // Remove the stock from the watchlist
        onRemoveFromWatchlist(stock);
        // Reload the page
        window.location.reload();
        // Close the modal
        onClose();
    };

    return (
        <div className="fixed inset-0 bg-transparent backdrop-blur-sm flex items-center justify-center z-50">
            <div className="bg-white rounded-lg p-8 max-w-2xl w-full mx-6 shadow-lg">
                {/* ------------------ Modal Header ------------------ */}
                <div className="flex items-center justify-between mb-4">
                    {/* ------------------ Modal Title ------------------ */}
                    <h2 className="text-xl font-bold">Add to Watchlist</h2>
                    {/* ------------------ Modal Close Button ------------------ */}
                    <button
                        onClick={onClose}
                        className="text-gray-500 hover:text-gray-700"
                    >
                        ✕
                    </button>
                </div>

                {/* ------------------ Modal Content ------------------ */}
                <div className="flex items-center justify-center mb-6">
                    {/* ------------------ Stock Information ------------------ */}
                    <div className="text-center">
                        {/* ------------------ Stock Symbol ------------------ */}
                        <h3 className="text-lg font-semibold mb-2">{stock.symbol}</h3>
                        {/* ------------------ Stock Company Name ------------------ */}
                        <p className="text-gray-600 mb-4">{stock.companyName}</p>
                        {/* ------------------ Stock Price ------------------ */}
                        <p className="text-blue-500 font-bold text-xl">{stock.price}</p>
                    </div>
                </div>

                {/* ------------------ Check if the stock is in the watchlist to display the star icon or/and the trash icon ------------------ */}
                {isInWatchlist ? (
                    <>
                        <div className="flex items-center justify-center mb-6 items-end gap-4">
                            {/* ------------------ Star Icon ------------------ */}
                            <button
                                onClick={handleStarClick}
                                className={`flex items-center justify-center p-4 rounded-full transition-all duration-200 ${isInWatchlist
                                    ? 'bg-yellow-100 text-yellow-500 ring-2 ring-yellow-500'
                                    : 'bg-gray-100 text-gray-400 hover:bg-gray-200'
                                    }`}
                            >
                                {isInWatchlist ? (
                                    <StarIcon className="h-8 w-8" />
                                ) : (
                                    <StarOutlineIcon className="h-8 w-8" />
                                )}
                            </button>

                            {/* ------------------ Delete Icon ------------------ */}
                            <button
                                onClick={handleRemoveFromWatchlist}
                                className="text-red-500 hover:text-red-700 p-2 rounded-full ring-2 ring-red-500 bg-red-100 p-4"
                            >
                                <TrashIcon className="h-8 w-8" />
                            </button>
                        </div>

                        {/* ------------------ Text for state of the stock in the watchlist ------------------ */}
                        <div className="text-center">
                            <p className="text-sm text-gray-600">
                                {isInWatchlist ? 'Stock is in your watchlist' : 'Click the star to add to your watchlist'}
                            </p>
                        </div>
                    </>
                ) : (
                    <>
                        {/* ------------------ Star Icon ------------------ */}
                        <div className="flex items-center justify-center mb-6">
                            <button
                                onClick={handleStarClick}
                                className={`flex items-center justify-center p-4 rounded-full transition-all duration-200 ${isInWatchlist
                                    ? 'bg-yellow-100 text-yellow-500 ring-2 ring-yellow-500'
                                    : 'bg-gray-100 text-gray-400 hover:bg-gray-200'
                                    }`}
                            >
                                {isInWatchlist ? (
                                    <StarIcon className="h-8 w-8" />
                                ) : (
                                    <StarOutlineIcon className="h-8 w-8" />
                                )}
                            </button>
                        </div>

                        {/* ------------------ Text for state of the stock in the watchlist ------------------ */}
                        <div className="text-center">
                            <p className="text-sm text-gray-600">
                                {isInWatchlist ? 'Stock is in your watchlist' : 'Click the star to add to your watchlist'}
                            </p>
                        </div>

                    </>
                )}


            </div>
        </div>
    )
}

export default AddToWatchlistModal;