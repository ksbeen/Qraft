// PostDetailPage.jsx

import { useState, useEffect } from 'react';
// useNavigate를 추가로 불러옵니다.
import { useParams, useNavigate, Link } from 'react-router-dom';
import axios from 'axios';

function PostDetailPage() {
  const { id } = useParams();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  // navigate 함수를 사용할 수 있도록 초기화합니다.
  const navigate = useNavigate();

  useEffect(() => {
    // ... 기존 fetchPost 함수는 그대로 ...
    const fetchPost = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/posts/${id}`);
        setPost(response.data);
      } catch (error) {
        console.error('게시글을 불러오는 데 실패했습니다:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchPost();
  }, [id]);

  // ▼▼▼▼▼ 이 함수를 추가해주세요 ▼▼▼▼▼
  // 삭제 버튼 클릭 시 실행될 함수
  const handleDelete = async () => {
    // 사용자에게 정말 삭제할 것인지 한번 더 물어봅니다.
    if (window.confirm('정말 이 게시글을 삭제하시겠습니까?')) {
      try {
        // 백엔드의 게시글 삭제 API를 호출합니다.
        await axios.delete(`http://localhost:8080/api/posts/${id}`);
        alert('게시글이 성공적으로 삭제되었습니다.');
        // 삭제 성공 후, 게시판 목록 페이지로 이동합니다.
        navigate('/posts');
      } catch (error) {
        console.error('게시글 삭제에 실패했습니다:', error);
        alert('게시글 삭제에 실패했습니다.');
      }
    }
  };
  // ▲▲▲▲▲ 이 함수를 추가합니다 ▲▲▲▲▲

  // ... 로딩 및 게시글 없음 처리는 그대로 ...
  if (loading) {
    return <div>로딩 중...</div>;
  }
  if (!post) {
    return <div>해당 게시글을 찾을 수 없습니다.</div>;
  }
  
  return (
    <div>
      <h2>{post.title}</h2>
      <p>작성자: {post.authorNickname}</p>
      <p>작성일: {new Date(post.createdAt).toLocaleString()}</p>
      <Link to={`/posts/${id}/edit`}>
        <button>수정</button>
      </Link>
      <button onClick={handleDelete}>삭제</button>
      <hr />
      <div style={{ whiteSpace: 'pre-wrap' }}>
        {post.content}
      </div>
    </div>
  );
}

export default PostDetailPage;