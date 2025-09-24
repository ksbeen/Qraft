package com.example.Qraft.service;

import com.example.Qraft.dto.PostCreateRequestDto;
import com.example.Qraft.dto.PostResponseDto;
import com.example.Qraft.dto.PostUpdateRequestDto;
import com.example.Qraft.entity.Post;
import com.example.Qraft.entity.PostReaction;
import com.example.Qraft.entity.User;
import com.example.Qraft.repository.CommentRepository;
import com.example.Qraft.repository.PostReactionRepository;
import com.example.Qraft.repository.PostRepository;
import com.example.Qraft.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final PostReactionRepository postReactionRepository;
    private final CommentRepository commentRepository;

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
     * 게시글 단건 조회 로직 (조회수 증가 없음)
     */
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));
        
        // 추천/비추천 수 조회
        Long recommendCount = postReactionRepository.countByPostIdAndReactionType(postId, PostReaction.ReactionType.RECOMMEND);
        Long opposeCount = postReactionRepository.countByPostIdAndReactionType(postId, PostReaction.ReactionType.OPPOSE);
        
        // 현재 사용자의 반응 조회
        PostReaction.ReactionType currentUserReaction = null;
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(userEmail)) {
            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            if (userOpt.isPresent()) {
                Optional<PostReaction> reactionOpt = postReactionRepository.findByPostIdAndUserId(postId, userOpt.get().getId());
                if (reactionOpt.isPresent()) {
                    currentUserReaction = reactionOpt.get().getReactionType();
                }
            }
        }
        
        return new PostResponseDto(post, recommendCount, opposeCount, currentUserReaction);
    }

    /**
     * 게시글 조회수 증가
     */
    @Transactional
    public void increaseViews(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));
        post.increaseViews();
        postRepository.save(post);
    }

    /**
     * 게시글 전체 목록 조회 로직 (최신순 정렬, 배치 처리로 N+1 쿼리 문제 해결)
     */
    @Transactional(readOnly = true)
    public List<PostResponseDto> findAll() {
        List<Post> posts = postRepository.findAllOrderByCreatedAtDesc();
        
        if (posts.isEmpty()) {
            return List.of();
        }
        
        // 게시글 ID 목록 추출
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .collect(Collectors.toList());
        
        // 배치로 모든 게시글의 추천/비추천 수 조회
        List<Object[]> reactionCounts = postReactionRepository.findReactionCountsByPostIds(postIds);
        Map<Long, Map<PostReaction.ReactionType, Long>> reactionCountMap = reactionCounts.stream()
                .collect(Collectors.groupingBy(
                    row -> (Long) row[0], // postId
                    Collectors.toMap(
                        row -> (PostReaction.ReactionType) row[1], // reactionType
                        row -> (Long) row[2] // count
                    )
                ));
        
        // 현재 사용자 정보 조회
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<Long, PostReaction.ReactionType> userReactionMap = Map.of();
        
        if (!"anonymousUser".equals(userEmail)) {
            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            if (userOpt.isPresent()) {
                // 배치로 현재 사용자의 모든 게시글에 대한 반응 조회
                List<PostReaction> userReactions = postReactionRepository.findByPostIdsAndUserId(postIds, userOpt.get().getId());
                userReactionMap = userReactions.stream()
                        .collect(Collectors.toMap(
                            reaction -> reaction.getPost().getId(),
                            PostReaction::getReactionType
                        ));
            }
        }
        
        // 최종 Map을 final로 만들어 람다에서 사용 가능하게 함
        final Map<Long, PostReaction.ReactionType> finalUserReactionMap = userReactionMap;
        
        return posts.stream()
                .map(post -> {
                    Long postId = post.getId();
                    Map<PostReaction.ReactionType, Long> postReactionCounts = reactionCountMap.getOrDefault(postId, Map.of());
                    
                    Long recommendCount = postReactionCounts.getOrDefault(PostReaction.ReactionType.RECOMMEND, 0L);
                    Long opposeCount = postReactionCounts.getOrDefault(PostReaction.ReactionType.OPPOSE, 0L);
                    PostReaction.ReactionType currentUserReaction = finalUserReactionMap.get(postId);
                    
                    return new PostResponseDto(post, recommendCount, opposeCount, currentUserReaction);
                })
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
     * 게시글 삭제 로직 (권한 검사 포함, 관련 데이터 cascade 삭제)
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
        
        // 5. 게시글 삭제 전 관련 데이터들을 먼저 삭제합니다.
        // 5-1. 해당 게시글의 모든 추천/비추천 데이터 삭제
        postReactionRepository.deleteByPostId(postId);
        
        // 5-2. 해당 게시글의 모든 댓글 삭제
        commentRepository.deleteByPostId(postId);
        
        // 6. 관련 데이터 삭제 후 게시글을 삭제합니다.
        postRepository.delete(post);
    }

    /**
     * 게시글 추천/비추천 처리
     */
    @Transactional
    public void reactToPost(Long postId, PostReaction.ReactionType reactionType, String userEmail) {
        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));
        
        // 사용자 확인
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. email=" + userEmail));
        
        // 기존 반응 확인
        Optional<PostReaction> existingReaction = postReactionRepository.findByPostIdAndUserId(postId, user.getId());
        
        if (existingReaction.isPresent()) {
            PostReaction reaction = existingReaction.get();
            if (reaction.getReactionType() == reactionType) {
                // 같은 반응이면 삭제 (토글)
                postReactionRepository.delete(reaction);
            } else {
                // 다른 반응이면 변경
                reaction.updateReactionType(reactionType);
                postReactionRepository.save(reaction);
            }
        } else {
            // 새로운 반응 생성
            PostReaction newReaction = PostReaction.builder()
                    .post(post)
                    .user(user)
                    .reactionType(reactionType)
                    .build();
            postReactionRepository.save(newReaction);
        }
    }

    /**
     * 게시글의 추천/비추천 수 조회
     */
    @Transactional(readOnly = true)
    public PostResponseDto getPostWithReactions(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));
        
        Long recommendCount = postReactionRepository.countByPostIdAndReactionType(postId, PostReaction.ReactionType.RECOMMEND);
        Long opposeCount = postReactionRepository.countByPostIdAndReactionType(postId, PostReaction.ReactionType.OPPOSE);
        
        // 현재 사용자의 반응 확인
        PostReaction.ReactionType currentUserReaction = null;
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(userEmail)) {
            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            if (userOpt.isPresent()) {
                Optional<PostReaction> reactionOpt = postReactionRepository.findByPostIdAndUserId(postId, userOpt.get().getId());
                if (reactionOpt.isPresent()) {
                    currentUserReaction = reactionOpt.get().getReactionType();
                }
            }
        }
        
        return new PostResponseDto(post, recommendCount, opposeCount, currentUserReaction);
    }
}