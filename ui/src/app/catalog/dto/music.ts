import { MediaItem } from './media_item';

export interface Music extends MediaItem {
    artist: string;
    type: string;
    label: string;
    asin: string;
    id: number;
}
