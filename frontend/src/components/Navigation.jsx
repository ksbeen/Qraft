import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Navigation.css';

function Navigation() {
  const [activeDropdown, setActiveDropdown] = useState(null);
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const menuItems = [
    {
      name: '질문',
      submenu: [
        { name: '질문 목록', path: '/questions' },
        { name: '즐겨찾기 질문', path: '/questions/favorites' }
      ]
    },
    {
      name: '면접 연습',
      submenu: [
        { name: '면접 연습 시작', path: '/interview' },
        { name: '답변 다시보기', path: '/interview/history' }
      ]
    },
    {
      name: '커뮤니티',
      submenu: [
        { name: '커뮤니티', path: '/posts' },
        { name: '면접 후기', path: '/interview-reviews' }
      ]
    },
    {
      name: '마이페이지',
      submenu: [
        { name: '내 정보', path: '/my-info' },
        { name: '활동 내역', path: '/my-logs' }
      ]
    },
    {
      name: '도움말',
      submenu: [
        { name: '이용 안내', path: '/help/guide' },
        { name: '자주 묻는 질문', path: '/help/faq' }
      ]
    }
  ];

  const handleDropdownToggle = (index) => {
    setActiveDropdown(activeDropdown === index ? null : index);
  };

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <nav className="navigation">
      <div className="nav-container">
        {/* 전체 메뉴 (햄버거) */}
        <div className="menu-toggle">
          <button className="hamburger-btn" onClick={toggleMenu}>
            <span className="hamburger-line"></span>
            <span className="hamburger-line"></span>
            <span className="hamburger-line"></span>
          </button>
          
          {/* 통합된 전체 메뉴 드롭다운 */}
          {isMenuOpen && (
            <div className="unified-menu-dropdown">
              <div className="unified-menu-content">
                {menuItems.map((category, index) => (
                  <div key={index} className="unified-menu-category">
                    <div className="unified-category-title">{category.name}</div>
                    <div className="unified-category-items">
                      {category.submenu.map((item, subIndex) => (
                        <Link 
                          key={subIndex}
                          to={item.path}
                          className="unified-category-item"
                          onClick={() => setIsMenuOpen(false)}
                        >
                          {item.name}
                        </Link>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* 메인 네비게이션 메뉴 */}
        <div className="nav-menu">
          {menuItems.map((item, index) => (
            <div 
              key={index}
              className="nav-item"
              onMouseEnter={() => !isMenuOpen && setActiveDropdown(index)}
              onMouseLeave={() => !isMenuOpen && setActiveDropdown(null)}
            >
              <button 
                className="nav-link"
                onClick={() => handleDropdownToggle(index)}
              >
                {item.name}
                <svg className="dropdown-arrow" width="12" height="12" viewBox="0 0 24 24" fill="none">
                  <path d="M6 9L12 15L18 9" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
                </svg>
              </button>
              
              {activeDropdown === index && !isMenuOpen && (
                <div className="dropdown-menu">
                  {item.submenu.map((subitem, subindex) => (
                    <Link 
                      key={subindex}
                      to={subitem.path}
                      className="dropdown-link"
                      onClick={() => setActiveDropdown(null)}
                    >
                      {subitem.name}
                    </Link>
                  ))}
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </nav>
  );
}

export default Navigation;
