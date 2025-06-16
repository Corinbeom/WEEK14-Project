import type { Board } from './Board';
import type { CommentType } from './Comment';

export interface Profile {
    userId: string;
    userName: string;
    userEmail: string;
    profileImage: string;
    myPosts: Board[];
    myComments: CommentType[];
    myLikes: Board[];
}
