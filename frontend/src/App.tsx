// import React from 'react';
import {BrowserRouter, Routes, Route, Link, useNavigate} from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import Header from './components/Header/Header';
import GetBoards from "./pages/Board/GetBoards.tsx";
import PostBoard from "./pages/Board/PostBoard.tsx";
import BoardDetail from "./pages/Board/BoardDetail.tsx";
import EditBoard from "./pages/Board/EditBoard.tsx";
import './assets/styles/Form.css';
import './Home.css';
import {useState} from "react";
import type {UserLoginDTO} from "./types/User.ts";
import {loginUser} from "./api/user.ts";
import ProfilePage from "./pages/ProfilePage.tsx";

function Home() {
    const [form, setForm] = useState<UserLoginDTO>({
        userId: '',
        password: '',
    });
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.currentTarget;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async () => {
        try {
            const response = await loginUser(form);
            alert(`로그인 성공 ! ${response.userName}님 환영합니다 🫡`);
            localStorage.setItem('token', response.token);
            localStorage.setItem('userName', response.userName);
            navigate('/'); // 홈으로 이동 (혹은 로그인페이지로)
            window.location.reload();
        } catch (err) {
            alert('로그인 실패');
        }
    };

    return (
        <div className="main-section">
            <div className="main-text">
                <h1>
                    <span className="line line1">Your Journey</span><br/>
                    <span className="line line2">Starts Here,</span><br/>
                    <span className="line line3">Grow Together.</span>
                </h1>
            </div>

            { token ? (
                <div className="right-menu">
                    <div className="menu-card-right" onClick={() => navigate("/boards")}>
                        게시글 조회
                    </div>
                    <div className="menu-card-left" onClick={() => navigate("/boards/new")}>
                        게시글 작성
                    </div>
                </div>
            ) : (
                <div className="form-container">
                    <h2>로그인</h2>
                    <div className="form-group">
                        <label>아이디</label>
                        <input name="userId" value={form.userId} onChange={handleChange} placeholder="아이디"/>
                    </div>
                    <div className="form-group">
                        <label>비밀번호</label>
                        <input type="password" name="password" value={form.password} onChange={handleChange} placeholder="비밀번호"/>
                    </div>
                    <button className="form-button" onClick={handleSubmit}>로그인</button>

                    <div className="signup-link">
                        아직 회원이 아니신가요? <Link to="/register">회원가입</Link>
                    </div>
                </div>
            )}
        </div>
    );
}

function App() {
    return (
        <BrowserRouter basename="/">
            <Header/>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/boards" element={<GetBoards/>}/>
                <Route path="/boards/new" element={<PostBoard/>}/>
                <Route path="/board/:id" element={<BoardDetail/>}/>
                <Route path="/board/:id/edit" element={<EditBoard/>} />
                <Route path="/profile" element={<ProfilePage />} />
                <Route path="/boards/:type" element={<GetBoards />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
