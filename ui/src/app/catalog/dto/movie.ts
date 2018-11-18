import { MediaItem } from './media_item';

export interface Movie extends MediaItem {
    director: string;
    producers: Array<string>;
    actors: Array<string>;
    dubbed: Array<string>;
    language: string;
    subtitles: string;
    runTime: number;
}
