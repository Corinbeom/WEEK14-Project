import { getBoardsByType } from '../../api/board';
import type { Board, BoardType } from '../../types/Board';
import { useEffect, useState } from "react";
import { useNavigate, useParams } from 'react-router-dom';
import './GetBoard.css';

export default function GetBoards() {
    const [boards, setBoards] = useState<Board[]>([]);
    const navigate = useNavigate();
    const { type } = useParams<{ type: BoardType }>();
    const token = localStorage.getItem('token') || '';

    useEffect(() => {
        const fetchBoards = async () => {
            try {
                const data = await getBoardsByType(type, token);
                setBoards(data);
            } catch (error) {
                console.error('게시글 조회 실패:', error);
            }
        };
        fetchBoards();
    }, [type, token]);

    const handleClick = (id: number) => {
        navigate(`/board/${id}`);
    };

    return (
        <div className="board-list-container">
            <h2>게시글 목록 {type ? `(${type})` : '(전체)'}</h2>

            {/* 카테고리 탭 추가 */}
            <div className="board-tabs">
                <a onClick={() => navigate('/boards')}>전체</a>
                <a onClick={() => navigate('/boards/JUNGLE')}>정글</a>
                <a onClick={() => navigate('/boards/GAME_LAB')}>게임랩</a>
                <a onClick={() => navigate('/boards/GAME_TECH_LAB')}>게임테크랩</a>
            </div>

            {boards
                .sort((a, b) => new Date(b.createdTime).getTime() - new Date(a.createdTime).getTime())
                .map((board) => (
                    <div key={board.id} className="board-card" onClick={() => handleClick(board.id)}>
                        <div className="board-title">{board.title}</div>
                        <div className="board-meta">
                            작성자: {board.author} | 조회수: {board.viewCount} | 좋아요: {board.likeCount} <br />
                            작성일: {new Date(board.createdTime).toLocaleString()}
                        </div>
                        <div className="board-content">{board.content}</div>
                    </div>
                ))}
        </div>
    );
}
