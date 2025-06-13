// src/pages/LoginPage.tsx

import React, { useState } from 'react';
import { loginUser } from '../api/user';
import type { UserLoginDTO } from '../types/User';

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
            alert(`로그인 성공! 토큰: ${response.data.token}`);
        } catch (err) {
            alert('로그인 실패');
        }
    };

    return (
        <div>
            <h1>로그인</h1>
        <input name="userId" value={form.userId} onChange={handleChange} placeholder="아이디" />
    <input name="password" value={form.password} onChange={handleChange} placeholder="비밀번호" />
    <button onClick={handleSubmit}>로그인</button>
        </div>
);
}
