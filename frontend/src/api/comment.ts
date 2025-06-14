import axios from 'axios';

// 댓글 조회
export async function getComments(postId: number) {
    const response = await axios.get(`/api/comments/${postId}`);
    return response.data;
}

// 댓글 작성
export async function createComment(postId: number, comment: { author: string; content: string; }, token: string) {
    const response = await axios.post(`/api/comments/${postId}`, comment, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });
    return response.data;
}

// 댓글 삭제
export async function deleteComment(commentId: number, token: string) {
    await axios.delete(`/api/comments/${commentId}`, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });
}
