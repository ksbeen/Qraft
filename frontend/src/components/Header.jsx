import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

function Header() {
  const [searchQuery, setSearchQuery] = useState('');

  return (
    <header className="header">
      <div className="header-container">
        {/* 로고 */}
        <div className="logo-section">
          <h1 className="logo">
            <Link to="/" className="logo-link">
              <span className="logo-qraft">Qraft</span>
            </Link>
          </h1>
        </div>

        {/* 검색창 */}
        <div className="search-section">
          <div className="search-container">
            <input 
              type="text"
              placeholder="면접질문, 인턴, 채용 정보를 검색해보세요"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="search-input"
            />
            <button className="search-button">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                <path d="M21 21L16.514 16.506M19 10.5A8.5 8.5 0 111 10.5a8.5 8.5 0 0118 0z" 
                      stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
              </svg>
            </button>
          </div>
        </div>

        {/* 광고 배너 공간 */}
        <div className="ad-banner">
          <div className="ad-content">
            <span className="ad-highlight">참시 이틀 차 미리고 공개!</span>
            <span className="ad-brand">kakao</span>
            <div className="ad-subtitle">[카카오그룹] 2026 신입직군 공개</div>
          </div>
        </div>
      </div>
    </header>
  );
}

export default Header;
