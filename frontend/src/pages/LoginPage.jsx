// src/pages/LoginPage.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';

function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const requestData = { email, password };
    try {
      const response = await apiClient.post('/api/users/login', requestData);
      const token = response.data.token;
      localStorage.setItem('authToken', token);
      alert('로그인에 성공했습니다.');
      // 페이지 새로고침으로 NavBar 상태 업데이트
      window.location.href = '/';
    } catch (error) {
      console.error('로그인 실패:', error);
      alert('이메일 또는 비밀번호를 확인해주세요.');
    }
  };

  return (
    <div className="form-container">
      <h2 className="form-title">로그인</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="email">이메일 주소</label>
          <input type="email" id="email" required value={email} onChange={(e) => setEmail(e.target.value)} />
        </div>
        <div className="form-group">
          <label htmlFor="password">비밀번호</label>
          <input type="password" id="password" required value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <button type="submit" className="form-button">로그인</button>
      </form>
    </div>
  );
}
export default LoginPage;