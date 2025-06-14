import React, { useState } from 'react';
import { registerUser } from '../api/user';
import type { UserRegisterDTO } from '../types/User';
import '../assets/styles/Form.css';

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
        <div className="register-container">
            <h2>회원가입</h2>

            <div className="form-group">
                <label>닉네임</label>
                <input name="userName" value={form.userName} onChange={handleChange} placeholder="닉네임"/>
            </div>

            <div className="form-group">
                <label>아이디</label>
                <input name="userId" value={form.userId} onChange={handleChange} placeholder="아이디"/>
            </div>

            <div className="form-group">
                <label>이메일</label>
                <input name="userEmail" value={form.userEmail} onChange={handleChange} placeholder="이메일"/>
            </div>

            <div className="form-group">
                <label>비밀번호</label>
                <input type="password" name="password" value={form.password} onChange={handleChange}
                       placeholder="비밀번호"/>
                <small className="password-rule">
                    비밀번호는 대문자와 특수문자를 포함하여 최소 8자 이상이어야 합니다.
                </small>
            </div>

            <div className="form-group">
                <label>생년월일</label>
                <input type="date" name="userBirth" value={form.userBirth} onChange={handleChange}/>
            </div>

            <button onClick={handleSubmit}>가입하기</button>
        </div>

    );
}
