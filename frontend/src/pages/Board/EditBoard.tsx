import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import type { BoardType } from '../../types/Board';
import { getBoard, updateBoard } from '../../api/board';

export default function EditBoard() {
    const params = useParams();
    const id = params.id;
    const navigate = useNavigate();
    const token = localStorage.getItem('token') || '';

    const [type, setType] = useState<BoardType>('JUNGLE');
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [imageUrl, setImageUrl] = useState('');
    const [selectedFile, setSelectedFile] = useState<File | null>(null);

    useEffect(() => {
        if (!id) return;
        const fetchData = async () => {
            const board = await getBoard(parseInt(id));
            setType(board.type);
            setTitle(board.title);
            setContent(board.content);
            setImageUrl(board.imageUrl || '');
        };
        fetchData();
    }, [id]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('type', type);
        formData.append('title', title);
        formData.append('content', content);
        // 파일 선택 안한 경우 → 기존 이미지 URL 유지
        if (selectedFile) {
            formData.append('imageFile', selectedFile);
        } else {
            formData.append('imageUrl', imageUrl);  // 기존 URL 그대로 보냄
        }

        await updateBoard(parseInt(id!), formData, token);
        alert('수정 완료!');
        navigate(`/board/${id}`);
    };

    return (
        <div>
            <h2>게시글 수정</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>게시판 타입:</label>
                    <select value={type} onChange={(e) => setType(e.target.value as BoardType)}>
                        <option value="JUNGLE">정글</option>
                        <option value="GAME_LAB">게임랩</option>
                        <option value="GAME_TECH_LAB">게임테크랩</option>
                    </select>
                </div>
                <div>
                    <label>제목:</label>
                    <input value={title} onChange={(e) => setTitle(e.target.value)} required />
                </div>
                <div>
                    <label>본문:</label>
                    <textarea value={content} onChange={(e) => setContent(e.target.value)} required />
                </div>
                <div>
                    <label>현재 이미지:</label>
                    {imageUrl && <img src={imageUrl} alt="기존 이미지" style={{maxWidth: '200px'}} />}
                </div>
                <div>
                    <label>새 이미지 선택:</label>
                    <input type="file" onChange={(e) => {
                        if (e.target.files?.[0]) {
                            setSelectedFile(e.target.files[0]);
                        }
                    }} />
                </div>
                <button type="submit">수정하기</button>
            </form>
        </div>
    );
}
