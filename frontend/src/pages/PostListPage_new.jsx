// src/pages/PostListPage.jsx
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import apiClient from '../api/apiClient';
import './MainPage.css';

function PostListPage() {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await apiClient.get('/api/posts');
        setPosts(response.data);
      } catch (error) {
        console.error('게시글 목록을 불러오는 데 실패했습니다:', error);
      }
    };
    fetchPosts();
  }, []);

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px', paddingBottom: '16px', borderBottom: '1px solid #e9ecef'}}>
            <h1 style={{fontSize: '24px', fontWeight: '600', color: '#333', margin: 0}}>커뮤니티</h1>
            <Link to="/posts/new" style={{padding: '10px 20px', backgroundColor: '#007bff', color: 'white', textDecoration: 'none', borderRadius: '6px', fontWeight: '500'}}>
              새 글 작성
            </Link>
          </div>
          
          <div style={{display: 'flex', flexDirection: 'column', gap: '12px'}}>
            {posts.map((post) => (
              <Link key={post.id} to={`/posts/${post.id}`} style={{background: 'white', border: '1px solid #e9ecef', borderRadius: '8px', padding: '20px', textDecoration: 'none', color: 'inherit', transition: 'all 0.2s'}}>
                <h3 style={{fontSize: '18px', fontWeight: '600', marginBottom: '8px', color: '#333'}}>{post.title}</h3>
                <div style={{display: 'flex', gap: '15px', fontSize: '14px', color: '#6c757d'}}>
                  <span>작성자: {post.authorNickname}</span>
                  <span>작성일: {new Date(post.createdAt).toLocaleString()}</span>
                </div>
              </Link>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default PostListPage;
