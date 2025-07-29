import type { StockDto } from "./StockDto";

export interface WatchlistDto {
    id: number;
    createdAt: string;
    stock: StockDto;
    username: string;
}
