// src/api/apiClient.js
import axios from 'axios';

// axios 인스턴스를 생성합니다.
const apiClient = axios.create({
  // 모든 요청에 기본적으로 포함될 서버의 주소를 설정합니다.
  baseURL: 'http://localhost:8080',
});

export default apiClient;