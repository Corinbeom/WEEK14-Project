import { Link, useNavigate } from 'react-router-dom';
import './Header.css';

export default function Header() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userName');
        // 혹시 추가 저장된 userId 등 있으면 이것도 삭제
        localStorage.removeItem('userId');
        alert("로그아웃 완료")
        navigate('/'); // 홈으로 이동 (혹은 로그인페이지로)
        window.location.reload();
    };

    return (
        <header className="header">
            <div className="left">
                <Link to="/">
                    <img src="/jungle_logo.png" alt="로고" className="logo" />
                </Link>
            </div>

            <div className="center">
                <nav className="nav">
                    {token ? (
                        <>
                            <a href="#" className="logout-link" onClick={e => {
                                e.preventDefault();
                                handleLogout();
                            }}>
                                로그아웃
                            </a>
                            <Link to="/profile">프로필 관리</Link>
                        </>
                    ) : (
                        <>
                            {/*<Link to="/login">로그인</Link>*/}
                            <Link to="/register">회원가입</Link>
                        </>
                    )}
                </nav>
            </div>
        </header>
    );
}
