import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import type { Board } from '../../types/Board';
import {getBoard, toggleLike, increaseViewCount, deleteBoard} from '../../api/board';
import {getComments, createComment, deleteComment} from '../../api/comment';
import type {CommentType} from "../../types/Comment";


export default function BoardDetail() {
    const { id } = useParams<{ id: string }>();
    const [board, setBoard] = useState<Board | null>(null);
    const navigate = useNavigate();
    const token = localStorage.getItem('token') || '';
    const userId = localStorage.getItem('userId') || '';  // 로그인 시 userId도 저장해놨다고 가정
    const [comments, setComments] = useState<CommentType[]>([]);
    const [newComment, setNewComment] = useState('');

    useEffect(() => {
        if (!id) return;

        const fetchBoard = async () => {
            const boardData = await getBoard(parseInt(id));
            setBoard(boardData);
        };

        const increaseView = async () => {
            if (token) {
                await increaseViewCount(parseInt(id), token);
            }
        };

        const fetchComments = async () => {
            const commentsData = await getComments(parseInt(id));
            setComments(commentsData);
        };

        fetchBoard();
        fetchComments();
        increaseView();
    }, [id]);


    const handleToggleLike = async () => {
        if (!token) {
            alert('로그인 후 이용해주세요.');
            return;
        }
        await toggleLike(parseInt(id!), token);
        const updated = await getBoard(parseInt(id!));
        setBoard(updated);
    };

    const handleDelete = async () => {
        if (!window.confirm('정말 삭제하시겠습니까?')) return;
        await deleteBoard(parseInt(id!), token);
        alert('삭제 완료');
        navigate('/boards');
    };

    const handleCommentSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!token) {
            alert('로그인 후 작성 가능합니다.');
            return;
        }
        await createComment(parseInt(id!), { author: userId, content: newComment }, token);
        setNewComment('');
        const updatedComments = await getComments(parseInt(id!));
        setComments(updatedComments);
    };

    const handleDeleteComment = async (commentId: number) => {
        if (!window.confirm('정말 삭제하시겠습니까?')) return;
        await deleteComment(commentId, token);
        const updatedComments = await getComments(parseInt(id!));
        setComments(updatedComments);
    };

    if (!board) return <div>Loading...</div>;

    const isAuthor = userId === board.author;

    return (
        <div>
            <h2>{board.title}</h2>
            <p>작성자: {board.author}</p>
            <p>조회수: {board.viewCount} | 좋아요: {board.likeCount}</p>
            {board.imageUrl && <img src={board.imageUrl} alt="첨부 이미지"/>}
            <p>{board.content}</p>

            <button onClick={handleToggleLike}>좋아요</button>

            {isAuthor && (
                <div>
                    <button onClick={() => navigate(`/board/${id}/edit`)}>수정</button>
                    <button onClick={handleDelete}>삭제</button>
                </div>
            )}
            <div>
                <h3>댓글</h3>
                <form onSubmit={handleCommentSubmit}>
        <textarea
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            required
        />
                    <button type="submit">작성</button>
                </form>

                <ul>
                    {comments.map((comment) => (
                        <li key={comment.id}>
                            <p>{comment.author}: {comment.content}</p>
                            <p>{new Date(comment.createdTime).toLocaleString()}</p>
                            {comment.author === userId && (
                                <button onClick={() => handleDeleteComment(comment.id)}>삭제</button>
                            )}
                        </li>
                    ))}
                </ul>
            </div>

        </div>

    );
}
