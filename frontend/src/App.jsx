// src/App.jsx
import { Route, Routes } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import MainPage from './pages/MainPage.jsx';
import SignUpPage from './pages/SignUpPage';
import LoginPage from './pages/LoginPage';
import PostListPage from './pages/PostListPage';
import PostDetailPage from './pages/PostDetailPage';
import NewPostPage from './pages/NewPostPage';
import EditPostPage from './pages/EditPostPage';
import InterviewLobbyPage from './pages/InterviewLobbyPage';
import InterviewSessionPage from './pages/InterviewSessionPage';
import MyPracticeLogsPage from './pages/MyPracticeLogsPage';
import PracticeLogDetailPage from './pages/PracticeLogDetailPage';

function App() {
  return (
    <AuthProvider>
      <div>
        <main style={{ maxWidth: '100%', margin: '0', padding: '0' }}>
          <Routes>
            <Route path="/" element={<MainPage />} />
            <Route path="/signup" element={<SignUpPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/posts" element={<PostListPage />} />
            <Route path="/posts/new" element={<NewPostPage />} />
            <Route path="/posts/:id" element={<PostDetailPage />} />
            <Route path="/posts/:id/edit" element={<EditPostPage />} />
            <Route path="/interview" element={<InterviewLobbyPage />} />
            <Route path="/interview/:id" element={<InterviewSessionPage />} />
            <Route path="/my-logs" element={<MyPracticeLogsPage />} />
            <Route path="/practice-logs/:id" element={<PracticeLogDetailPage />} />
          </Routes>
        </main>
      </div>
    </AuthProvider>
  );
}
export default App;