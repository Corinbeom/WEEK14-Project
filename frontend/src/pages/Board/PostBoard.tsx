import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import { postBoard } from '../../api/board';
import type { BoardType } from '../../types/Board';
import '../../assets/styles/Form.css';
import './PostBoard.css';

export default function PostBoard() {
    const [type, setType] = useState<BoardType>('JUNGLE');
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const navigate = useNavigate();

    const allowedTypes = ['image/png', 'image/jpeg', 'image/svg+xml'];

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

        const token = localStorage.getItem('token');
        if (!token) {
            alert('로그인 후 이용해주세요.');
            return;
        }

        if (!selectedFile) {
            alert('이미지 파일을 선택해주세요.');
            return;
        }

        const formData = new FormData();
        formData.append('type', type);
        formData.append('title', title);
        formData.append('content', content);
        formData.append('imageFile', selectedFile);

        await postBoard(formData, token);
        alert('게시글이 등록되었습니다.');
        navigate('/boards');
    };

    return (
        <div className="post-container">
            <h2>게시글 작성</h2>
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
                    <label>이미지 파일 (png, jpg, svg):</label>
                    <input
                        type="file"
                        accept="image/png, image/jpeg, image/svg+xml"
                        onChange={handleFileChange}
                        required
                    />
                </div>

                <button type="submit" className="form-button">등록하기</button>
            </form>
        </div>
    );
}
