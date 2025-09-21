import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);

  // 페이지 로드시 localStorage에서 로그인 상태 복원
  useEffect(() => {
    const savedLoginState = localStorage.getItem('isLoggedIn');
    const savedUser = localStorage.getItem('user');
    
    if (savedLoginState === 'true' && savedUser) {
      setIsLoggedIn(true);
      setUser(JSON.parse(savedUser));
    }
  }, []);

  const login = (username, password) => {
    // 간단한 로그인 검증 (실제로는 API 호출)
    if (username && password) {
      const userData = { username };
      setIsLoggedIn(true);
      setUser(userData);
      
      // localStorage에 상태 저장
      localStorage.setItem('isLoggedIn', 'true');
      localStorage.setItem('user', JSON.stringify(userData));
      
      return true;
    }
    return false;
  };

  const logout = () => {
    setIsLoggedIn(false);
    setUser(null);
    
    // localStorage에서 상태 제거
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('user');
  };

  const value = {
    isLoggedIn,
    user,
    login,
    logout
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
