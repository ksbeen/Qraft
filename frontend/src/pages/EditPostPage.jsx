// src/pages/EditPostPage.jsx
import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import '../pages/MainPage.css';

function EditPostPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const response = await apiClient.get(`/api/posts/${id}`);
        setTitle(response.data.title);
        setContent(response.data.content);
      } catch (error) {
        console.error('게시글 정보를 불러오는 데 실패했습니다:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchPost();
  }, [id]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    const updatedData = { title, content };
    try {
      await apiClient.put(`/api/posts/${id}`, updatedData);
      alert('게시글이 성공적으로 수정되었습니다.');
      navigate(`/posts/${id}`);
    } catch (error) {
      console.error('게시글 수정에 실패했습니다:', error);
      alert('게시글 수정에 실패했습니다.');
    }
  };

  if (loading) return <div>로딩 중...</div>;

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          <div className="form-container" style={{maxWidth: '800px'}}>
            <h2 className="form-title">게시글 수정</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="title">제목</label>
                <input id="title" type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />
              </div>
              <div className="form-group">
                <label htmlFor="content">내용</label>
                <textarea id="content" value={content} onChange={(e) => setContent(e.target.value)} rows="15" required />
              </div>
              <button type="submit" className="form-button">수정 완료</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
export default EditPostPage;