// src/pages/PostDetailPage.jsx
import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import apiClient from '../api/apiClient';

function PostDetailPage() {
  const { id } = useParams();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const response = await apiClient.get(`/api/posts/${id}`);
        setPost(response.data);
      } catch (error) {
        console.error('게시글을 불러오는 데 실패했습니다:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchPost();
  }, [id]);

  const handleDelete = async () => {
    if (window.confirm('정말 이 게시글을 삭제하시겠습니까?')) {
      try {
        await apiClient.delete(`/api/posts/${id}`);
        alert('게시글이 성공적으로 삭제되었습니다.');
        navigate('/posts');
      } catch (error) {
        console.error('게시글 삭제에 실패했습니다:', error);
        alert('게시글 삭제에 실패했습니다.');
      }
    }
  };

  if (loading) return <div>로딩 중...</div>;
  if (!post) return <div>해당 게시글을 찾을 수 없습니다.</div>;
  
  return (
    <div className="post-card">
      <div className="post-detail-header">
        <h1 className="post-detail-title">{post.title}</h1>
        <div className="post-detail-meta">
          <span>작성자: {post.authorNickname}</span>
          <span>작성일: {new Date(post.createdAt).toLocaleString()}</span>
        </div>
      </div>
      <div className="post-detail-actions">
        <Link to={`/posts/${id}/edit`}>
          <button className="action-button">수정</button>
        </Link>
        <button className="action-button delete" onClick={handleDelete}>삭제</button>
      </div>
      <div className="post-detail-content">
        {post.content}
      </div>
    </div>
  );
}
export default PostDetailPage;