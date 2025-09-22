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
            <div>
              {posts.map((post) => (
                <Link key={post.id} to={`/posts/${post.id}`}>
                  <div className="post-card">
                    <h3 className="post-card-title">{post.title}</h3>
                    <p className="post-card-author">작성자: {post.authorNickname}</p>
                  </div>
                </Link>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
export default PostListPage;