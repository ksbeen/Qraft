// src/pages/NewPostPage.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';

function NewPostPage() {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const requestData = { title, content, boardType: 'FREE' };
    try {
      await apiClient.post('/api/posts', requestData);
      alert('게시글이 성공적으로 등록되었습니다.');
      navigate('/posts');
    } catch (error) {
      console.error('게시글 생성 실패:', error);
      alert('게시글 생성에 실패했습니다.');
    }
  };

  return (
    <div className="form-container" style={{maxWidth: '800px'}}>
      <h2 className="form-title">새 게시글 작성</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="title">제목</label>
          <input id="title" type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />
        </div>
        <div className="form-group">
          <label htmlFor="content">내용</label>
          <textarea id="content" value={content} onChange={(e) => setContent(e.target.value)} rows="15" required />
        </div>
        <button type="submit" className="form-button">등록</button>
      </form>
    </div>
  );
}
export default NewPostPage;