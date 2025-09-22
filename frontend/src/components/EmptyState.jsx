// src/components/EmptyState.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import './EmptyState.css';

function EmptyState({ 
  icon = '📝',
  title = '아직 데이터가 없습니다',
  description = '',
  actionText = null,
  actionLink = null,
  onAction = null
}) {
  return (
    <div className="empty-state-container">
      <div className="empty-state-icon">{icon}</div>
      <h3 className="empty-state-title">{title}</h3>
      {description && <p className="empty-state-description">{description}</p>}
      {(actionText && (actionLink || onAction)) && (
        <div className="empty-state-action">
          {actionLink ? (
            <Link to={actionLink} className="empty-action-button">
              {actionText}
            </Link>
          ) : (
            <button className="empty-action-button" onClick={onAction}>
              {actionText}
            </button>
          )}
        </div>
      )}
    </div>
  );
}

export default EmptyState;
