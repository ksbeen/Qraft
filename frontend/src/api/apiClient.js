// src/api/apiClient.js
import axios from 'axios';

// axios 인스턴스를 생성합니다.
const apiClient = axios.create({
  // 모든 요청에 기본적으로 포함될 서버의 주소를 설정합니다.
  baseURL: 'http://localhost:8080',
});

// 요청 인터셉터 (Request Interceptor)
// 모든 API 요청을 보내기 전에 이 코드가 먼저 실행됩니다.
apiClient.interceptors.request.use(
  (config) => {
    // 1. 브라우저의 localStorage에서 'authToken'을 가져옵니다.
    const token = localStorage.getItem('authToken');

    // 2. 토큰이 존재하면,
    if (token) {
      // 3. HTTP 요청 헤더(headers)의 'Authorization'에 'Bearer' 토큰을 설정합니다.
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    // 4. 설정이 완료된 요청(config)을 반환하여 API 호출을 계속 진행합니다.
    return config;
  },
  (error) => {
    // 요청 에러가 발생했을 때의 처리
    return Promise.reject(error);
  }
);

export default apiClient;