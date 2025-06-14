import { Link } from 'react-router-dom';
import './Header.css';

export default function Header() {
    return (
        <header className="header">
            <div className="left">
                <Link to="/">
                    <img src="/jungle_logo.png" alt="로고" className="logo" />
                </Link>
            </div>
            <div className="center">
                <nav className="nav">
                    <Link to="/login">로그인</Link>
                    <Link to="/register">회원가입</Link>
                </nav>
            </div>
        </header>
    );
}
