// src/pages/InterviewSessionPage.jsx
import { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';

function InterviewSessionPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [interviewSet, setInterviewSet] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [status, setStatus] = useState('ready');
  const [loading, setLoading] = useState(true);
  const videoRef = useRef(null);
  const mediaRecorderRef = useRef(null);
  const recordedChunksRef = useRef([]);
  const streamRef = useRef(null);

  useEffect(() => {
    const fetchInterviewData = async () => {
      try {
        const response = await apiClient.get(`/api/interview-sets/${id}`);
        setInterviewSet(response.data);
        setQuestions(response.data.questions);
      } catch (error) {
        console.error('면접 데이터를 불러오는 데 실패했습니다:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchInterviewData();
  }, [id]);

  const handleStartInterview = async () => {
    setStatus('inProgress');
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
      streamRef.current = stream;
      if (videoRef.current) {
        videoRef.current.srcObject = stream;
      }
      recordedChunksRef.current = [];
      const recorder = new MediaRecorder(stream);
      mediaRecorderRef.current = recorder;
      recorder.ondataavailable = (event) => {
        if (event.data.size > 0) {
          recordedChunksRef.current.push(event.data);
        }
      };
      recorder.onstop = async () => {
        const videoBlob = new Blob(recordedChunksRef.current, { type: 'video/webm' });
        const formData = new FormData();
        formData.append('video', videoBlob, `interview-${id}.webm`);
        try {
          alert('영상을 업로드 중입니다. 잠시만 기다려주세요...');
          const uploadResponse = await apiClient.post('/api/upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
          });
          const videoUrl = uploadResponse.data;
          const logData = { interviewSetId: id, videoUrl: videoUrl };
          await apiClient.post('/api/practice-logs', logData);
          alert('면접 영상이 성공적으로 저장되었습니다.');
        } catch (error) {
          console.error('영상 업로드 또는 기록 저장에 실패했습니다:', error);
          alert('영상 저장에 실패했습니다.');
        } finally {
            recordedChunksRef.current = [];
        }
      };
      recorder.start();
    } catch (error) {
      console.error('웹캠을 시작하는 데 실패했습니다:', error);
      alert('웹캠/마이크 접근 권한을 허용해주세요.');
      setStatus('ready');
    }
  };

  const handleNextQuestion = () => {
    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(prevIndex => prevIndex + 1);
    } else {
      handleEndInterview();
    }
  };

  const handleEndInterview = () => {
    if (mediaRecorderRef.current && mediaRecorderRef.current.state === "recording") {
      mediaRecorderRef.current.stop();
    }
    setStatus('finished');
    if (streamRef.current) {
      streamRef.current.getTracks().forEach(track => track.stop());
    }
  };

  if (loading) return <div>로딩 중...</div>;

  if (status === 'ready') {
    return (
      <div style={{textAlign: 'center', marginTop: '50px'}}>
        <h1 className="page-title">{interviewSet?.name}</h1>
        <p style={{margin: '20px 0 30px', color: 'var(--text-secondary-color)'}}>
          면접을 시작할 준비가 되면 아래 버튼을 눌러주세요.
        </p>
        <button className="form-button" style={{width: '200px'}} onClick={handleStartInterview}>면접 시작</button>
      </div>
    );
  }

  if (status === 'finished') {
    return (
      <div style={{textAlign: 'center', marginTop: '50px'}}>
        <h1 className="page-title">면접이 종료되었습니다.</h1>
        <p style={{margin: '20px 0 30px', color: 'var(--text-secondary-color)'}}>
          수고하셨습니다. 면접 기록이 저장되었습니다.
        </p>
        <button className="form-button" style={{width: '250px'}} onClick={() => navigate('/my-logs')}>내 기록 확인하기</button>
      </div>
    );
  }

  return (
    <div className="session-container">
      <h1 className="page-title">{interviewSet.name}</h1>
      <video ref={videoRef} autoPlay playsInline muted className="session-video"/>
      <div className="question-box">
        <p className="question-number">질문 {currentQuestionIndex + 1} / {questions.length}</p>
        <p className="question-content">
          {questions.length > 0 ? questions[currentQuestionIndex].content : '질문이 없습니다.'}
        </p>
      </div>
      <div className="session-actions">
        <button className="primary" onClick={handleNextQuestion}>다음 질문</button>
        <button className="secondary" onClick={handleEndInterview}>면접 종료</button>
      </div>
    </div>
  );
}
export default InterviewSessionPage;