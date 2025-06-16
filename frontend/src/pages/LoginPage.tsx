// src/pages/LoginPage.tsx

import React, { useState } from 'react';
import { loginUser } from '../api/user';
import type { UserLoginDTO } from '../types/User';
// import '../assets/styles/Form.css';

export default function LoginPage() {
    const [form, setForm] = useState<UserLoginDTO>({
        userId: '',
        password: '',
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.currentTarget;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async () => {
        try {
            const response = await loginUser(form);
            alert(`로그인 성공 ! ${response.userName}님 환영합니다 🫡`);
        } catch (err) {
            alert('로그인 실패');
        }
    };

    return (
        <div className="form-container">
            <h2>로그인</h2>
            <div className="form-group">
                <label>아이디</label>
                <input name="userId" value={form.userId} onChange={handleChange} placeholder="아이디" />
            </div>
            <div className="form-group">
                <label>비밀번호</label>
                <input type="password" name="password" value={form.password} onChange={handleChange} placeholder="비밀번호" />
            </div>
            <button onClick={handleSubmit}>로그인</button>
        </div>
    );
}