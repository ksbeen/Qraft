// src/pages/InterviewReviewListPage.jsx
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import apiClient from '../api/apiClient';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import EmptyState from '../components/EmptyState';
import '../pages/MainPage.css';

function InterviewReviewListPage() {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchReviews = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get('/api/interview-reviews');
      setReviews(response.data);
    } catch (error) {
      console.error('면접 후기를 불러오는 데 실패했습니다:', error);
      setError('면접 후기를 불러오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // 날짜 포맷 함수
  const formatDate = (dateString) => {
    if (!dateString) return '-';
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now - date);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays === 1) {
      return date.toLocaleDateString('ko-KR', {
        hour: '2-digit',
        minute: '2-digit'
      });
    } else {
      return date.toLocaleDateString('ko-KR', {
        month: '2-digit',
        day: '2-digit'
      });
    }
  };

  useEffect(() => {
    fetchReviews();
  }, []);

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          <div className="page-header">
            <h1 className="page-title">면접 후기</h1>
            <Link to="/interview-reviews/new">
              <button className="write-button">후기 작성</button>
            </Link>
          </div>
          
          {loading && <Loading message="면접 후기를 불러오는 중..." />}
          
          {error && (
            <ErrorMessage 
              message={error} 
              onRetry={fetchReviews}
              retryText="다시 불러오기"
            />
          )}
          
          {!loading && !error && reviews.length === 0 && (
            <EmptyState
              icon="💼"
              title="작성된 면접 후기가 없습니다"
              description="첫 번째 면접 후기를 작성해보세요!"
              actionText="후기 작성"
              actionLink="/interview-reviews/new"
            />
          )}
          
          {!loading && !error && reviews.length > 0 && (
            <div className="interview-board">
              <div className="interview-board-header">
                <div className="board-category">회사</div>
                <div className="board-position">직무</div>
                <div className="board-title">제목</div>
                <div className="board-author">글쓴이</div>
                <div className="board-date">날짜</div>
                <div className="board-views">조회</div>
                <div className="board-likes">추천</div>
              </div>

              <div className="interview-board-content">
                {reviews.map((review) => (
                  <Link key={review.id} to={`/interview-reviews/${review.id}`} className="interview-board-row">
                    <div className="board-category">
                      <span className="company-tag">{review.company}</span>
                    </div>
                    <div className="board-position">
                      <span className="position-tag">{review.position}</span>
                    </div>
                    <div className="board-title">
                      <span className="post-title">{review.title}</span>
                    </div>
                    <div className="board-author">
                      <div className="author-info">
                        <div className="author-avatar">
                          <span className="avatar-icon">👤</span>
                        </div>
                        <span className="author-name">{review.authorNickname}</span>
                      </div>
                    </div>
                    <div className="board-date">
                      {formatDate(review.createdAt)}
                    </div>
                    <div className="board-views">{review.views || 0}</div>
                    <div className="board-likes">{review.recommendCount || 0}</div>
                  </Link>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default InterviewReviewListPage;