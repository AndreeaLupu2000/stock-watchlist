import type { StockDto } from "../dto/StockDto";
import { StarIcon } from "@heroicons/react/24/solid";
import { StarIcon as StarOutlineIcon } from "@heroicons/react/24/outline";

interface StockCardProps {
    stock: StockDto;
    onAddToWatchlist: (stock: StockDto) => void;
    isInWatchlist?: boolean;
}

const StockCard = ({ stock, onAddToWatchlist, isInWatchlist = false }: StockCardProps) => {
    return (
        <div className="flex gap-4 bg-white p-6 rounded-lg shadow-md">
            {/* ------------------ Add to Watchlist Button ------------------ */}
            <button
                className={`flex items-center justify-center px-3 py-1.5 rounded-full text-xs font-semibold transition-all duration-200 ${isInWatchlist
                        ? 'bg-yellow-100 text-yellow-500 ring-1 ring-yellow-500 hover:bg-yellow-200'
                        : 'bg-[#EAF2FE] text-[#1A97FE] ring-1 ring-[#1A97FE] hover:bg-[#D4E8FD]'
                    }`}
                onClick={() => onAddToWatchlist(stock)}
            >
                {isInWatchlist ? (
                    <StarIcon className="h-4 w-4" />
                ) : (
                    <StarOutlineIcon className="h-4 w-4" />
                )}
            </button>

            {/* ------------------ Stock Information ------------------ */}
            <div className="flex items-end gap-2 w-full">
                {/* ------------------ Stock Symbol ------------------ */}
                <div className="flex justify-center items-center gap-2 w-1/2 h-14">
                    <h1 className="text-center text-2xl font-bold">{stock.symbol}</h1>
                </div>

                {/* ------------------ Stock Company Name & Price ------------------ */}
                <div className="flex flex-col items-center gap-2 w-1/2">
                    <h1 className="text-xl text-gray-500 text-center h-14 text-center">{stock.companyName.substring(0, 28) + ".."}</h1>
                    <h1 className="text-xl font-bold text-blue-500">{stock.price}</h1>
                </div>
            </div>
        </div>
    )
}

export default StockCard;