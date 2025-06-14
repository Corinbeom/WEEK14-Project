export type BoardType = 'JUNGLE' | 'GAME_LAB' | 'GAME_TECH_LAB';

export interface Board {
    id: number;
    type: BoardType;
    title: string;
    content: string;
    imageUrl?: string;
    author: string;
    viewCount: number;
    likeCount: number;
    createdTime: string;
    modifiedTime: string;
}
