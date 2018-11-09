import { ItemSpecification } from './item_specification';

export interface PrintedItem extends ItemSpecification {
    publisher: string;
    pubDate: string;
    lang: string;
    isbn10: string;
    isbn13: string;
}
