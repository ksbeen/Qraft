package com.example.Qraft.service;

import com.example.Qraft.dto.PostCreateRequestDto;
import com.example.Qraft.dto.PostResponseDto;
import com.example.Qraft.dto.PostUpdateRequestDto;
import com.example.Qraft.entity.Post;
import com.example.Qraft.entity.User;
import com.example.Qraft.repository.PostRepository;
import com.example.Qraft.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Service: 이 클래스가 비즈니스 로직을 담당하는 서비스 계층의 컴포넌트(Bean)임을 나타냅니다.
 * @RequiredArgsConstructor: final로 선언된 필드에 대한 생성자를 자동으로 만들어, 의존성 주입(DI)을 간편하게 합니다.
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 생성 로직
     * @param userEmail JWT 토큰에서 추출한 현재 로그인된 사용자의 이메일
     * @param requestDto 게시글 생성에 필요한 데이터 (title, content, boardType)
     * @return 생성된 Post 객체
     */
    @Transactional
    public Post create(String userEmail, PostCreateRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. email=" + userEmail));
        
        Post newPost = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .board_type(requestDto.getBoardType())
                .user(user)
                .build();
        
        return postRepository.save(newPost);
    }

    /**
     * 게시글 단건 조회 로직
     */
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));
        return new PostResponseDto(post);
    }

    /**
     * 게시글 전체 목록 조회 로직
     */
    @Transactional(readOnly = true)
    public List<PostResponseDto> findAll() {
        return postRepository.findAll()
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 수정 로직 (권한 검사 포함)
     * @param postId 수정할 게시글의 ID
     * @param requestDto 새로운 제목과 내용을 담은 DTO
     * @return 수정된 게시글의 ID
     */
    @Transactional
    public Long update(Long postId, PostUpdateRequestDto requestDto) {
        // 1. 수정할 게시글이 DB에 존재하는지 확인합니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));
        
        // --- 인가(Authorization) 로직 ---
        // 2. 현재 요청을 보낸 사용자(로그인한 사용자)의 이메일을 가져옵니다.
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // 3. 게시글 작성자의 이메일과 현재 로그인한 사용자의 이메일이 다른지 확인합니다.
        if (!post.getUser().getEmail().equals(userEmail)) {
            // 4. 다르다면, 권한이 없다는 예외를 발생시켜 작업을 중단합니다.
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        
        // 5. 권한 검사를 통과하면, 게시글 내용을 수정합니다.
        post.update(requestDto.getTitle(), requestDto.getContent());
        return postId;
    }

    /**
     * 게시글 삭제 로직 (권한 검사 포함)
     * @param postId 삭제할 게시글의 ID
     */
    @Transactional
    public void delete(Long postId) {
        // 1. 삭제할 게시글이 DB에 존재하는지 확인합니다.
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));
            
        // --- 인가(Authorization) 로직 ---
        // 2. 현재 요청을 보낸 사용자(로그인한 사용자)의 이메일을 가져옵니다.
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // 3. 게시글 작성자의 이메일과 현재 로그인한 사용자의 이메일이 다른지 확인합니다.
        if (!post.getUser().getEmail().equals(userEmail)) {
            // 4. 다르다면, 권한이 없다는 예외를 발생시켜 작업을 중단합니다.
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        
        // 5. 권한 검사를 통과하면, 게시글을 삭제합니다.
        postRepository.delete(post);
    }
}