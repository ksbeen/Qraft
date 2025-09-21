// src/components/NavBar.jsx
import { Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import './NavBar.css';

function NavBar() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate();

  // 컴포넌트 마운트 시 로그인 상태 확인
  useEffect(() => {
    const token = localStorage.getItem('authToken');
    setIsLoggedIn(!!token);
  }, []);

  // 로그아웃 처리 함수
  const handleLogout = () => {
    localStorage.removeItem('authToken');
    setIsLoggedIn(false);
    alert('로그아웃되었습니다.');
    navigate('/');
  };

  return (
    <header className="navbar-header">
      <nav className="navbar">
        <Link to="/" className="navbar-brand">Qraft</Link>
        <div className="navbar-links">
          <Link to="/interview">면접 연습</Link>
          <Link to="/posts">커뮤니티</Link>
          {isLoggedIn && <Link to="/my-logs">마이페이지</Link>}
        </div>
        <div className="navbar-auth">
          {isLoggedIn ? (
            <button onClick={handleLogout} className="logout-button">로그아웃</button>
          ) : (
            <>
              <Link to="/login">로그인</Link>
              <Link to="/signup">회원가입</Link>
            </>
          )}
        </div>
      </nav>
    </header>
  );
}
export default NavBar;