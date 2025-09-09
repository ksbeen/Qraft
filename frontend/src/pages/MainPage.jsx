// src/pages/MainPage.jsx
import React from 'react';
import './MainPage.css'; // MainPage를 위한 CSS 파일

// 아이콘 데이터 (나중에 DB에서 불러오거나 더 동적으로 만들 수 있습니다)
const services = [
  { icon: '🎥', title: '면접 연습 시작' },
  { icon: '💬', title: '내 답변 보기' },
  { icon: '📋', title: '질문 목록 보기' },
  { icon: '✍️', title: '커뮤니티 글쓰기' },
  { icon: '📊', title: '나의 학습 통계' },
  { icon: '⭐', title: '즐겨찾는 질문' },
  { icon: '🎁', title: '추천 질문 받기' },
];

function MainPage() {
  return (
    <div className="main-page-container">
      {/* --- 상단 검색 영역 --- */}
      <section className="search-section">
        <div className="search-box">
          <select className="search-dropdown">
            <option>전체</option>
          </select>
          <input type="text" className="search-input" placeholder="필요한 서비스를 찾아보세요" />
          <button className="search-button">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M11 19C15.4183 19 19 15.4183 19 11C19 6.58172 15.4183 3 11 3C6.58172 3 3 6.58172 3 11C3 15.4183 6.58172 19 11 19Z" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              <path d="M21 21L16.65 16.65" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
          </button>
        </div>
        <div className="quick-links">
          <strong>추천검색어:</strong>
          <a href="#">면접 질문</a>
          <a href="#">면접 연습</a>
          <a href="#">커뮤니티</a>
          <a href="#">면접 후기</a>
        </div>
      </section>

      {/* --- 하단 서비스 바로 가기 영역 --- */}
      <section className="services-section">
        <h2 className="section-title">자주찾는 서비스</h2>
        <div className="service-grid">
          {services.map((service, index) => (
            <div key={index} className="service-card">
              <div className="service-icon">{service.icon}</div>
              <div className="service-title">{service.title}</div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}

export default MainPage;