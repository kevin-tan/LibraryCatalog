import { PrintedItem } from './printed_item';

export interface Book extends PrintedItem {
    author: string;
    format: string;
    pages: number;
}
