// src/pages/Board/PostBoard.tsx

// import React, { useState } from 'react';
import { postBoard } from '../../api/board';
import type { BoardType } from '../../types/Board';
import {useState} from "react";
import { useNavigate } from 'react-router-dom';

export default function PostBoard() {
    const [type, setType] = useState<BoardType>('JUNGLE');
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const navigate = useNavigate();
    const [selectedFile, setSelectedFile] = useState<File | null>(null);





    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const token = localStorage.getItem('token');
        if (!token) {
            alert('로그인 후 이용해주세요.');
            return;
        }

        if (!selectedFile) {
            alert('파일을 선택해주세요.');
            return;
        }

        const formData = new FormData();
        formData.append('type', type);
        formData.append('title', title);
        formData.append('content', content);
        formData.append('imageFile', selectedFile);  // ★ 여기가 핵심 ★

        await postBoard(formData, token);
        alert('업로드 성공');
        navigate('/boards');
    };

    return (
        <div>
            <h2>게시글 작성</h2>
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
                    <label>이미지 URL:</label>
                    <input
                        type="file"
                        onChange={(e) => {
                            if (e.target.files?.[0]) {
                                setSelectedFile(e.target.files[0]);
                            }
                        }}
                        required
                    />
                </div>
                <button type="submit">등록하기</button>
            </form>
        </div>
    );
}
