import React, { useState } from 'react';
import { registerUser } from '../api/user';
import type { UserRegisterDTO } from '../types/User';

export default function RegisterPage() {
    const [form, setForm] = useState<UserRegisterDTO>({
        userName: '',
        userId: '',
        userEmail: '',
        password: '',
        userBirth: '',
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.currentTarget;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async () => {
        try {
            await registerUser(form);
            alert('회원가입 성공');
        } catch (err) {
            alert('회원가입 실패');
        }
    };

    return (
        <div>
            <h1>회원가입</h1>
            <input name="userName" value={form.userName} onChange={handleChange} placeholder="이름" />
            <input name="userId" value={form.userId} onChange={handleChange} placeholder="아이디" />
            <input name="userEmail" value={form.userEmail} onChange={handleChange} placeholder="이메일" />
            <input name="password" value={form.password} onChange={handleChange} placeholder="비밀번호" />
            <input name="userBirth" value={form.userBirth} onChange={handleChange} placeholder="생년월일" />
            <button onClick={handleSubmit}>가입하기</button>
        </div>
    );
}
