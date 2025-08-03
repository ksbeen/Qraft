// PostListPage.jsx

import { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom'; // Link를 react-router-dom에서 불러옵니다.

function PostListPage() {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/posts');
        setPosts(response.data);
      } catch (error) {
        console.error('게시글 목록을 불러오는 데 실패했습니다:', error);
      }
    };

    fetchPosts();
  }, []);

  return (
    <div>
      <h2>게시판</h2>
      <Link to="/posts/new">
        <button>글쓰기</button>
      </Link>
      <div>
        {posts.map((post) => (
          // ▼▼▼▼▼ 이 부분을 수정합니다 ▼▼▼▼▼
          // 각 게시글을 Link 컴포넌트로 감싸줍니다.
          // to={`/posts/${post.id}`}는 '/posts/1', '/posts/2' 와 같은 경로를 만들어줍니다.
          <Link key={post.id} to={`/posts/${post.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
            <div style={{ border: '1px solid #ccc', margin: '10px', padding: '10px' }}>
              <h3>{post.title}</h3>
              <p>작성자: {post.authorNickname}</p>
            </div>
          </Link>
          // ▲▲▲▲▲ 이 부분을 수정합니다 ▲▲▲▲▲
        ))}
      </div>
    </div>
  );
}

export default PostListPage;