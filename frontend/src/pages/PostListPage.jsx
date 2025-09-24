// src/pages/PostListPage.jsx
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import apiClient from '../api/apiClient';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import EmptyState from '../components/EmptyState';
import '../pages/MainPage.css';

function PostListPage() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchPosts = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get('/api/posts');
      setPosts(response.data);
    } catch (error) {
      console.error('게시글 목록을 불러오는 데 실패했습니다:', error);
      setError('게시글 목록을 불러오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

// 카테고리 매핑 함수
  const getCategoryInfo = (boardType) => {
    switch(boardType) {
      case 'INFO':
        return { text: '정보', className: 'info' };
      case 'FREE':
        return { text: '자유', className: 'general' };
      default:
        return { text: '일반', className: 'general' };
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
    fetchPosts();
  }, []);

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          <div className="page-header">
            <h1 className="page-title">커뮤니티</h1>
            <Link to="/posts/new">
              <button className="write-button">글쓰기</button>
            </Link>
          </div>
          
          {loading && <Loading message="게시글을 불러오는 중..." />}
          
          {error && (
            <ErrorMessage 
              message={error} 
              onRetry={fetchPosts}
              retryText="다시 불러오기"
            />
          )}
          
          {!loading && !error && posts.length === 0 && (
            <EmptyState
              icon="📝"
              title="작성된 게시글이 없습니다"
              description="첫 번째 게시글을 작성해보세요!"
              actionText="글쓰기"
              actionLink="/posts/new"
            />
          )}
          
          {!loading && !error && posts.length > 0 && (
            <div className="community-board">
              <div className="board-header">
                <div className="board-category">분류</div>
                <div className="board-title">제목</div>
                <div className="board-author">글쓴이</div>
                <div className="board-date">날짜</div>
                <div className="board-views">조회</div>
                <div className="board-likes">추천</div>
              </div>
              
              <div className="board-content">
                {posts.map((post) => {
                  const categoryInfo = getCategoryInfo(post.boardType);
                  return (
                    <Link key={post.id} to={`/posts/${post.id}`} className="board-row">
                      <div className="board-category">
                        <span className={`category-tag ${categoryInfo.className}`}>
                          {categoryInfo.text}
                        </span>
                      </div>
                      <div className="board-title">
                        <span className="post-title">{post.title}</span>
                        {post.hasAttachment && <span className="attachment-icon">📎</span>}
                      </div>
                      <div className="board-author">
                        <div className="author-info">
                          <div className="author-avatar">
                            <span className="avatar-icon">👤</span>
                          </div>
                          <span className="author-name">{post.authorNickname}</span>
                        </div>
                      </div>
                      <div className="board-date">
                        {formatDate(post.createdAt)}
                      </div>
                      <div className="board-views">{post.views || 0}</div>
                      <div className="board-likes">{post.recommendCount || 0}</div>
                    </Link>
                  );
                })}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
export default PostListPage;