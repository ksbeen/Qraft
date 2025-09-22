// src/components/ErrorMessage.jsx
import React from 'react';
import './ErrorMessage.css';

function ErrorMessage({ 
  message = '오류가 발생했습니다.', 
  onRetry = null,
  retryText = '다시 시도'
}) {
  return (
    <div className="error-container">
      <div className="error-icon">⚠️</div>
      <p className="error-message">{message}</p>
      {onRetry && (
        <button className="error-retry-button" onClick={onRetry}>
          {retryText}
        </button>
      )}
    </div>
  );
}

export default ErrorMessage;
