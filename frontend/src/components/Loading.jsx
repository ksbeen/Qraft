// src/components/Loading.jsx
import React from 'react';
import './Loading.css';

function Loading({ message = '로딩 중...', size = 'medium' }) {
  return (
    <div className="loading-container">
      <div className={`loading-spinner ${size}`}>
        <div className="loading-spinner-inner"></div>
      </div>
      <p className="loading-message">{message}</p>
    </div>
  );
}

export default Loading;
