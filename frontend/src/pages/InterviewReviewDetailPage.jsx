import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import apiClient from '../api/apiClient';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import '../pages/MainPage.css';

function InterviewReviewDetailPage() {
  const { id } = useParams();
  const [review, setReview] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');
  const [editingCommentId, setEditingCommentId] = useState(null);
  const [editContent, setEditContent] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userReaction, setUserReaction] = useState(null);
  const navigate = useNavigate();

  // 현재 로그인한 사용자 정보 가져오기
  const fetchCurrentUser = async () => {
    try {
      const token = localStorage.getItem('authToken');
      if (token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        try {
          const response = await apiClient.get('/api/users/me');
          setCurrentUser(response.data);
        } catch (apiError) {
          setCurrentUser({
            id: payload.userId || payload.id,
            email: payload.sub || payload.email,
            nickname: payload.nickname
          });
        }
      }
    } catch (error) {
      console.error('사용자 정보를 불러오는 데 실패했습니다:', error);
    }
  };

  const fetchReview = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get(`/api/interview-reviews/${id}`);
      setReview(response.data);
    } catch (error) {
      console.error('면접 후기를 불러오는 데 실패했습니다:', error);
      setError('면접 후기를 불러오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // 조회수 증가
  const increaseViews = async () => {
    try {
      await apiClient.post(`/api/interview-reviews/${id}/views`);
    } catch (error) {
      console.error('조회수 증가에 실패했습니다:', error);
    }
  };

  // 댓글 목록 가져오기
  const fetchComments = async () => {
    try {
      const response = await apiClient.get(`/api/interview-reviews/${id}/comments`);
      setComments(response.data);
    } catch (error) {
      console.error('댓글을 불러오는 데 실패했습니다:', error);
    }
  };

  // 댓글 작성
  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (!newComment.trim()) {
      alert('댓글 내용을 입력해주세요.');
      return;
    }

    try {
      await apiClient.post(`/api/interview-reviews/${id}/comments`, {
        content: newComment
      });
      setNewComment('');
      await fetchComments();
      await fetchReview(); // 댓글 수 업데이트를 위해
      alert('댓글이 성공적으로 작성되었습니다.');
    } catch (error) {
      console.error('댓글 작성에 실패했습니다:', error);
      alert('댓글 작성에 실패했습니다.');
    }
  };

  // 댓글 수정
  const handleCommentEdit = async (commentId) => {
    if (!editContent.trim()) {
      alert('댓글 내용을 입력해주세요.');
      return;
    }

    try {
      await apiClient.put(`/api/interview-reviews/comments/${commentId}`, {
        content: editContent
      });
      setEditingCommentId(null);
      setEditContent('');
      await fetchComments();
      alert('댓글이 성공적으로 수정되었습니다.');
    } catch (error) {
      console.error('댓글 수정에 실패했습니다:', error);
      alert('댓글 수정에 실패했습니다.');
    }
  };

  // 댓글 삭제
  const handleCommentDelete = async (commentId) => {
    if (!window.confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
      return;
    }

    try {
      await apiClient.delete(`/api/interview-reviews/comments/${commentId}`);
      await fetchComments();
      await fetchReview(); // 댓글 수 업데이트를 위해
      alert('댓글이 성공적으로 삭제되었습니다.');
    } catch (error) {
      console.error('댓글 삭제에 실패했습니다:', error);
      alert('댓글 삭제에 실패했습니다.');
    }
  };

  // 추천/비추천 처리
  const handleReaction = async (reactionType) => {
    if (!currentUser) {
      alert('로그인이 필요합니다.');
      navigate('/login');
      return;
    }

    try {
      await apiClient.post(`/api/interview-reviews/${id}/reactions`, null, {
        params: { reactionType }
      });
      await fetchReview(); // 반응 수 업데이트를 위해
    } catch (error) {
      console.error('반응 처리에 실패했습니다:', error);
      alert('반응 처리에 실패했습니다.');
    }
  };

  // 면접 후기 삭제
  const handleDelete = async () => {
    if (!window.confirm('정말로 이 면접 후기를 삭제하시겠습니까?')) {
      return;
    }

    try {
      await apiClient.delete(`/api/interview-reviews/${id}`);
      alert('면접 후기가 성공적으로 삭제되었습니다.');
      navigate('/interview-reviews');
    } catch (error) {
      console.error('면접 후기 삭제에 실패했습니다:', error);
      alert('면접 후기 삭제에 실패했습니다.');
    }
  };

  // 현재 사용자가 게시글 작성자인지 확인
  const isAuthor = currentUser && review && currentUser.id === review.authorId;

  // 날짜 포맷 함수
  const formatDate = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  useEffect(() => {
    fetchCurrentUser();
    fetchReview();
    fetchComments();
    increaseViews(); // 조회수 증가
  }, [id]);

  if (loading) return (
    <div className="main-page">
      <Header />
      <Navigation />
      <div className="main-container">
        <Loading message="면접 후기를 불러오는 중..." />
      </div>
    </div>
  );

  if (error) return (
    <div className="main-page">
      <Header />
      <Navigation />
      <div className="main-container">
        <ErrorMessage 
          message={error} 
          onRetry={fetchReview}
          retryText="다시 불러오기"
        />
      </div>
    </div>
  );

  if (!review) return null;

  return (
    <div className="main-page">
      <Header />
      <Navigation />
      
      <div className="main-container">
        <div className="content-area">
          {/* 면접 후기 상세 내용 */}
          <div className="post-detail">
            {/* 게시글 헤더 */}
            <div className="post-header">
              <div className="post-category">
                <span>{review.company}</span>
                <span> - {review.position}</span>
              </div>
              <h1 className="post-title">{review.title}</h1>
              <div className="post-meta">
                <div className="post-date">{formatDate(review.createdAt)}</div>
                <div className="post-stats">
                  <span>조회 {review.views}</span>
                  <span>추천 {review.recommendCount}</span>
                  <span>댓글 {review.commentCount}</span>
                </div>
              </div>
            </div>

            {/* 작성자 정보 */}
            <div className="author-section">
              <div className="author-avatar">
                <span className="avatar-icon">👤</span>
              </div>
              <span className="author-name">{review.authorNickname}</span>
            </div>

            {/* 게시글 내용 */}
            <div className="post-content">
              <div className="content-text">
                {review.content.split('\n').map((line, index) => (
                  <p key={index}>{line}</p>
                ))}
              </div>
            </div>

            {/* 추천/비추천 버튼 */}
            <div className="post-reactions">
              <button 
                className={`reaction-button recommend ${userReaction === 'RECOMMEND' ? 'active' : ''}`}
                onClick={() => handleReaction('RECOMMEND')}
              >
                <span className="reaction-icon">👍</span>
                <span className="reaction-text">추천</span>
                <span className="reaction-count">{review.recommendCount || 0}</span>
              </button>
              <button 
                className={`reaction-button oppose ${userReaction === 'OPPOSE' ? 'active' : ''}`}
                onClick={() => handleReaction('OPPOSE')}
              >
                <span className="reaction-icon">👎</span>
                <span className="reaction-text">비추</span>
                <span className="reaction-count">{review.opposeCount || 0}</span>
              </button>
            </div>

            {/* 액션 버튼들 */}
            <div className="post-detail-actions">
              {isAuthor && (
                <>
                  <Link to={`/interview-reviews/${review.id}/edit`}>
                    <button className="action-button edit">수정</button>
                  </Link>
                  <button className="action-button delete" onClick={handleDelete}>삭제</button>
                </>
              )}
            </div>
          </div>

          {/* 댓글 섹션 */}
          <div className="comments-section">
            <h3 className="comments-title">댓글 {comments.length} 개</h3>
            
            {/* 댓글 작성 폼 */}
            {currentUser && (
              <form className="comment-form" onSubmit={handleCommentSubmit}>
                <div className="comment-input-container">
                  <div className="comment-author-avatar">
                    <span>{currentUser.nickname?.[0]?.toUpperCase() || 'U'}</span>
                  </div>
                  <textarea
                    className="comment-input"
                    placeholder="댓글을 작성하세요..."
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    rows="3"
                  />
                </div>
                <div className="comment-form-actions">
                  <button type="submit" className="comment-submit-btn">등록</button>
                </div>
              </form>
            )}

            {/* 댓글 목록 */}
            <div className="comments-list">
              {comments.map((comment) => (
                <div key={comment.id} className="comment-item">
                  <div className="comment-header">
                    <div className="comment-author-info">
                      <div className="comment-author-avatar">
                        <span>{comment.authorNickname?.[0]?.toUpperCase() || 'U'}</span>
                      </div>
                      <div className="comment-author-details">
                        <span className="comment-author-name">{comment.authorNickname}</span>
                        <span className="comment-date">
                          {new Date(comment.createdAt).toLocaleDateString('ko-KR', {
                            month: 'numeric',
                            day: 'numeric',
                            hour: '2-digit',
                            minute: '2-digit'
                          })}
                        </span>
                      </div>
                    </div>
                    {currentUser && currentUser.id === comment.authorId && (
                      <div className="comment-actions">
                        {editingCommentId === comment.id ? (
                          <>
                            <button 
                              className="comment-action-btn save"
                              onClick={() => handleCommentEdit(comment.id)}
                            >
                              저장
                            </button>
                            <button 
                              className="comment-action-btn cancel"
                              onClick={() => {
                                setEditingCommentId(null);
                                setEditContent('');
                              }}
                            >
                              취소
                            </button>
                          </>
                        ) : (
                          <>
                            <button 
                              className="comment-action-btn edit"
                              onClick={() => {
                                setEditingCommentId(comment.id);
                                setEditContent(comment.content);
                              }}
                            >
                              수정
                            </button>
                            <button 
                              className="comment-action-btn delete"
                              onClick={() => handleCommentDelete(comment.id)}
                            >
                              삭제
                            </button>
                          </>
                        )}
                      </div>
                    )}
                  </div>
                  <div className="comment-content">
                    {editingCommentId === comment.id ? (
                      <div className="comment-edit-form">
                        <textarea
                          className="comment-edit-input"
                          value={editContent}
                          onChange={(e) => setEditContent(e.target.value)}
                          rows="3"
                        />
                      </div>
                    ) : (
                      <p>{comment.content}</p>
                    )}
                  </div>
                </div>
              ))}
              
              {comments.length === 0 && (
                <div className="no-comments">
                  <p>첫 번째 댓글을 작성해보세요!</p>
                </div>
              )}
            </div>
          </div>

          {/* 이전/다음 글 네비게이션 */}
          <div className="post-navigation">
            <button className="nav-button prev">← 이전 글</button>
            <Link to="/interview-reviews" className="nav-button list">목록으로</Link>
            <button className="nav-button next">다음 글 →</button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default InterviewReviewDetailPage;