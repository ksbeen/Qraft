import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import { useAuth } from '../contexts/AuthContext';
import './MainPage.css';

function MainPage() {
  const { isLoggedIn, login } = useAuth();
  const [loginData, setLoginData] = useState({
    username: '',
    password: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setLoginData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleLogin = (e) => {
    e.preventDefault();
    const success = login(loginData.username, loginData.password);
    if (success) {
      setLoginData({ username: '', password: '' });
    }
  };

  const renderWelcomeContent = () => (
    <div className="content-box">
      <div className="box-header">
        <span>✅</span>
        <span>로그인 완료</span>
      </div>
      
      <div className="welcome-container">
        <h2 className="welcome-title">
          환영합니다!
        </h2>
        <p className="welcome-text">
          Qraft의 다양한 기능을 이용해보세요.
        </p>
        
        <div className="action-buttons">
          <Link to="/posts" className="action-button">
            📝 게시판 보기
          </Link>
          <Link to="/interview" className="action-button">
            🎤 면접 연습
          </Link>
        </div>
      </div>
    </div>
  );

  const renderLoginForm = () => (
    <div className="content-box">
      <div className="box-header">
        <span>👤</span>
        <span>게시판 로그인</span>
      </div>
      
      <form onSubmit={handleLogin} className="login-form">
        <input 
          type="text" 
          name="username"
          placeholder="아이디" 
          value={loginData.username}
          onChange={handleInputChange}
          className="login-input"
          required
        />
        <input 
          type="password" 
          name="password"
          placeholder="비밀번호" 
          value={loginData.password}
          onChange={handleInputChange}
          className="login-input"
          required
        />
        <button type="submit" className="login-button">
          로그인
        </button>
      </form>
      
      <div className="login-links">
        <Link to="/signup">회원가입</Link>
        <span style={{ color: '#9ca3af' }}>|</span>
        <Link to="/find-password">비밀번호 찾기</Link>
      </div>
    </div>
  );

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="sidebar">
          {isLoggedIn ? renderWelcomeContent() : renderLoginForm()}
        </div>
      </div>
    </div>
  );
}

export default MainPage;
