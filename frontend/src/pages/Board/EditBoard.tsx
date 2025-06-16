import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import type { BoardType } from '../../types/Board';
import { getBoard, updateBoard } from '../../api/board';
import '../../assets/styles/Form.css';
import './PostBoard.css';

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

    const allowedTypes = ['image/png', 'image/jpeg', 'image/svg+xml'];

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

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file) {
            if (!allowedTypes.includes(file.type)) {
                alert("지원하지 않는 파일 형식입니다. (png, jpg, svg만 지원)");
                return;
            }
            setSelectedFile(file);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('type', type);
        formData.append('title', title);
        formData.append('content', content);
        if (selectedFile) {
            formData.append('imageFile', selectedFile);
        } else {
            formData.append('imageUrl', imageUrl);
        }

        await updateBoard(parseInt(id!), formData, token);
        alert('수정 완료!');
        navigate(`/board/${id}`);
    };

    return (
        <div className="post-container">
            <h2>게시글 수정</h2>
            <form onSubmit={handleSubmit} className="post-form">
                <div className="form-group">
                    <label>게시판 타입:</label>
                    <select value={type} onChange={(e) => setType(e.target.value as BoardType)}>
                        <option value="JUNGLE">정글</option>
                        <option value="GAME_LAB">게임랩</option>
                        <option value="GAME_TECH_LAB">게임테크랩</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>제목:</label>
                    <input value={title} onChange={(e) => setTitle(e.target.value)} required />
                </div>

                <div className="form-group">
                    <label>본문:</label>
                    <textarea value={content} onChange={(e) => setContent(e.target.value)} required />
                </div>

                <div className="form-group">
                    <label>현재 이미지:</label>
                    {imageUrl && <img src={imageUrl} alt="기존 이미지" style={{ maxWidth: '200px' }} />}
                </div>

                <div className="form-group">
                    <label>새 이미지 파일 (png, jpg, svg):</label>
                    <input
                        type="file"
                        accept="image/png, image/jpeg, image/svg+xml"
                        onChange={handleFileChange}
                    />
                </div>

                <button type="submit" className="form-button">수정하기</button>
            </form>
        </div>
    );
}
