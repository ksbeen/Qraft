// src/components/NavBar.jsx
import { Link } from 'react-router-dom';
import './NavBar.css';

function NavBar() {
  return (
    <header className="navbar-header">
      <nav className="navbar">
        <Link to="/" className="navbar-brand">Qraft</Link>
        <div className="navbar-links">
          <Link to="/interview">면접 연습</Link>
          <Link to="/posts">커뮤니티</Link>
          <Link to="/my-logs">마이페이지</Link>
        </div>
        <div className="navbar-auth">
          <Link to="/login">로그인</Link>
          <Link to="/signup">회원가입</Link>
        </div>
      </nav>
    </header>
  );
}
export default NavBar;