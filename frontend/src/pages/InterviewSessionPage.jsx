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

      // --- 📹 녹화 중지 시 실행될 로직: 서버에 업로드 ---
      recorder.onstop = async () => {
        const videoBlob = new Blob(recordedChunksRef.current, { type: 'video/webm' });
        
        // 1. FormData 객체를 생성하여 영상 파일을 담습니다.
        //    서버의 @RequestParam("video")와 'video' 키 이름을 일치시켜야 합니다.
        const formData = new FormData();
        formData.append('video', videoBlob, `interview-${id}.webm`);

        try {
          // 2. 먼저 /api/upload 엔드포인트로 영상 파일을 업로드합니다.
          //    파일을 보낼 때는 Content-Type 헤더를 'multipart/form-data'로 설정해야 합니다.
          alert('영상을 업로드 중입니다. 잠시만 기다려주세요...');
          const uploadResponse = await apiClient.post('/api/upload', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
          
          // 3. 업로드 성공 시, 서버로부터 받은 파일 이름을 videoUrl로 사용합니다.
          const videoUrl = uploadResponse.data;

          // 4. /api/practice-logs 엔드포인트로 면접 기록 메타데이터를 저장합니다.
          const logData = {
            interviewSetId: id, // 어떤 면접 세트였는지
            videoUrl: videoUrl, // 업로드된 영상의 파일명
          };
          await apiClient.post('/api/practice-logs', logData);

          alert('면접 영상이 성공적으로 저장되었습니다.');

        } catch (error) {
          console.error('영상 업로드 또는 기록 저장에 실패했습니다:', error);
          alert('영상 저장에 실패했습니다.');
        } finally {
            // 녹화 데이터 초기화
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

  if (loading) return <div>면접 데이터를 불러오는 중입니다...</div>;
  
  if (status === 'ready') {
    return (
      <div>
        <h1>{interviewSet?.name}</h1>
        <p>면접을 시작할 준비가 되면 아래 버튼을 눌러주세요.</p>
        <button onClick={handleStartInterview}>면접 시작</button>
      </div>
    );
  }

  if (status === 'finished') {
    return (
      <div>
        <h1>면접이 종료되었습니다.</h1>
        <p>수고하셨습니다. 면접 기록이 저장되었습니다.</p>
        <button onClick={() => navigate('/interview')}>다른 면접 보러가기</button>
      </div>
    );
  }

  return (
    <div>
      <h1>{interviewSet.name}</h1>
      <hr />
      <video ref={videoRef} autoPlay playsInline muted style={{ width: '400px', border: '1px solid black' }}/>
      <div>
        <h3>질문 {currentQuestionIndex + 1} / {questions.length}</h3>
        <p>{questions.length > 0 ? questions[currentQuestionIndex].content : '질문이 없습니다.'}</p>
      </div>
      <div>
        <button onClick={handleNextQuestion}>다음 질문</button>
        <button onClick={handleEndInterview}>면접 종료</button>
      </div>
    </div>
  );
}

export default InterviewSessionPage;