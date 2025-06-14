import axios from 'axios';
import type { Board } from '../types/Board';

const BASE_URL = '/api/board';

// 게시글 전체 조회
export const getBoards = async (): Promise<Board[]> => {
    const res = await axios.get(BASE_URL);
    return res.data;
};

// 게시글 작성 (FormData 사용)
export const postBoard = async (formData: FormData, token: string) => {
    await axios.post(BASE_URL, formData, {
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
        },
    });
};

// 게시글 상세 조회
export const getBoard = async (boardId: number): Promise<Board> => {
    const token = localStorage.getItem('token') || '';
    const res = await axios.get(`${BASE_URL}/${boardId}`, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return res.data;
};

// 게시글 수정 (FormData 사용)
export const updateBoard = async (boardId: number, formData: FormData, token: string) => {
    await axios.put(`${BASE_URL}/${boardId}`, formData, {
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'multipart/form-data'
        }
    });
};

// 게시글 삭제
export const deleteBoard = async (boardId: number, token: string) => {
    await axios.delete(`${BASE_URL}/${boardId}`, {
        headers: { Authorization: `Bearer ${token}` }
    });
};

// 좋아요 토글
export const toggleLike = async (boardId: number, token: string) => {
    await axios.post(`${BASE_URL}/${boardId}/like`, {}, {
        headers: { Authorization: `Bearer ${token}` }
    });
};

// 조회수 증가
export const increaseViewCount = async (boardId: number, token: string) => {
    await axios.post(`${BASE_URL}/${boardId}/view`, {}, {
        headers: { Authorization: `Bearer ${token}` }
    });
};
