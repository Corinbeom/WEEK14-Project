import { getBoards } from '../../api/board';
import type { Board } from '../../types/Board';
import { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';

export default function GetBoards() {
    const [boards, setBoards] = useState<Board[]>([]);
    const navigate = useNavigate();  // 라우터 훅 사용

    useEffect(() => {
        const fetchBoards = async () => {
            try {
                const data = await getBoards();
                setBoards(data);
            } catch (error) {
                console.error('게시글 조회 실패:', error);
            }
        };
        fetchBoards();
    }, []);

    const handleClick = (id: number) => {
        navigate(`/board/${id}`);
    };

    return (
        <div>
            <h2>게시글 목록</h2>
            {boards.map((board) => (
                <div
                    key={board.id}
                    style={{ border: '1px solid #ccc', marginBottom: '1rem', padding: '1rem', cursor: 'pointer' }}
                    onClick={() => handleClick(board.id)}  // 카드 클릭 시 상세로 이동
                >
                    <h3>{board.title}</h3>
                    <p>작성자: {board.author}</p>
                    <p>조회수: {board.viewCount} / 좋아요: {board.likeCount}</p>
                    <p>작성일: {new Date(board.createdTime).toLocaleString()}</p>
                    <p>내용: {board.content}</p>
                </div>
            ))}
        </div>
    );
}
