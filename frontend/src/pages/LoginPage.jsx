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
      // 1. 백엔드에 로그인 요청을 보냅니다.
      const response = await apiClient.post('/api/users/login', requestData);

      // 2. 응답으로 받은 토큰을 추출합니다.
      const token = response.data.token;

      // 3. 받은 토큰을 브라우저의 localStorage에 저장합니다.
      //    localStorage는 브라우저를 닫았다 열어도 데이터가 유지되는 저장소입니다.
      localStorage.setItem('authToken', token);

      alert('로그인에 성공했습니다.');
      // 5. 로그인 성공 후, 게시판 목록 페이지로 이동합니다.
      navigate('/posts');

    } catch (error) {
      console.error('로그인 실패:', error);
      alert('이메일 또는 비밀번호를 확인해주세요.');
    }
  };

  return (
    <div>
      <h2>로그인</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>이메일</label>
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
        </div>
        <div>
          <label>비밀번호</label>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <button type="submit">로그인</button>
      </form>
    </div>
  );
}

export default LoginPage;