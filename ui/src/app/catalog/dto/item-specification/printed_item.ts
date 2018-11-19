import { ItemSpecification } from './item_specification';

export interface PrintedItem extends ItemSpecification {
    publisher: string;
    pubDate: string;
    language: string;
    isbn10: string;
    isbn13: string;
}
