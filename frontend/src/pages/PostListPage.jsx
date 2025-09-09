// src/pages/PostListPage.jsx
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import apiClient from '../api/apiClient';

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
    <div>
      <div className="page-header">
        <h1 className="page-title">커뮤니티</h1>
        <Link to="/posts/new">
          <button className="write-button">글쓰기</button>
        </Link>
      </div>
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
    </div>
  );
}
export default PostListPage;