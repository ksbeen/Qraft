import React, { useState } from "react";
import { Link } from "react-router-dom";
import Header from "../components/Header";
import Navigation from "../components/Navigation";
import { useAuth } from "../contexts/AuthContext";
import "./MainPage.css";

function MainPage() {
  const { isLoggedIn, login, logout, user } = useAuth();
  const [activeTab, setActiveTab] = useState("community");
  const [loginData, setLoginData] = useState({ email: "", password: "" });

  const handleLoginInputChange = (e) => {
    const { name, value } = e.target;
    setLoginData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    
    if (!loginData.email || !loginData.password) {
      alert("이메일과 비밀번호를 입력해주세요.");
      return;
    }

    const result = await login(loginData.email, loginData.password);
    if (result.success) {
      setLoginData({ email: "", password: "" });
      alert("로그인에 성공했습니다!");
    } else {
      alert(result.error);
    }
  };

  const handleLogout = () => {
    logout();
    setLoginData({ email: "", password: "" });
    alert("로그아웃되었습니다.");
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
    <div className="profile-card">
      <form onSubmit={handleLogin} className="login-form">
        <input 
          type="email" 
          name="email"
          placeholder="이메일" 
          value={loginData.email}
          onChange={handleLoginInputChange}
          className="login-input"
          required
        />
        <input 
          type="password" 
          name="password"
          placeholder="비밀번호" 
          value={loginData.password}
          onChange={handleLoginInputChange}
          className="login-input"
          required
        />
        <button type="submit" className="login-button">
          로그인
        </button>
      </form>
      
      <div className="login-links">
        <Link to="/find-id" className="login-link">아이디 찾기</Link>
        <span className="login-divider">|</span>
        <Link to="/find-password" className="login-link">비밀번호 찾기</Link>
        <span className="login-divider">|</span>
        <Link to="/signup" className="login-link">회원가입</Link>
      </div>
    </div>
  );

  const renderProfileCard = () => (
    <div className="profile-card">
      <div className="profile-header">
        <div className="profile-main">
          <div className="profile-avatar">
            <div className="avatar-circle">
              <span className="avatar-icon">👤</span>
            </div>
            <div className="avatar-settings">⚙️</div>
          </div>
          
        <div className="profile-details">
          <div className="profile-name-section">
            <span className="profile-nickname">{user?.nickname}</span>
            <div className="profile-id-badge">
              <span>Qraft ID 🔒</span>
            </div>
            <button onClick={handleLogout} className="logout-button">
              로그아웃 ↗
            </button>
          </div>
          <div className="profile-email">{user?.email}</div>
        </div>          <button onClick={handleLogout} className="logout-button">
            로그아웃 ↗
          </button>
        </div>
      </div>
    </div>
  );

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          {/* 여기에 메인 콘텐츠가 들어갈 예정 */}
        </div>
        
        <div className="profile-sidebar">
          {isLoggedIn ? renderProfileCard() : renderLoginForm()}
        </div>
      </div>
    </div>
  );
}

export default MainPage;
