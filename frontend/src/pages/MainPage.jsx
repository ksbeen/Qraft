import React, { useState } from "react";
import { Link } from "react-router-dom";
import Header from "../components/Header";
import Navigation from "../components/Navigation";
import { useAuth } from "../contexts/AuthContext";
import "./MainPage.css";

function MainPage() {
  const { isLoggedIn, login, logout } = useAuth();
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
    <div className="content-box">
      <div className="box-header">
        <span>👤</span>
        <span>게시판 로그인</span>
      </div>
      
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
