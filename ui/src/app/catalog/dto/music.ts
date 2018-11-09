import { MediaItem } from './media_item';

export interface Music extends MediaItem {
    type: string;
    label: string;
    asin: string;
}
