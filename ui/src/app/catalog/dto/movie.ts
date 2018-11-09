import { MediaItem } from './media_item';

export interface Movie extends MediaItem {
    director: string;
    producer: string;
    actor: string;
    lang: string;
    subtitles: string;
    runTime: string;
}
