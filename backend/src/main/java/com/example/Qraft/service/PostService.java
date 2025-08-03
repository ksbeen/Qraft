package com.example.Qraft.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; 
import java.util.stream.Collectors; 

import com.example.Qraft.dto.PostCreateRequestDto;
import com.example.Qraft.entity.Post;
import com.example.Qraft.entity.User;
import com.example.Qraft.repository.PostRepository;
import com.example.Qraft.repository.UserRepository;
import com.example.Qraft.dto.PostResponseDto;
import com.example.Qraft.dto.PostUpdateRequestDto;

import lombok.RequiredArgsConstructor;

// @Service: 이 클래스가 비즈니스 로직을 처리하는 Service 계층임을 나타냅니다.
@Service
// @RequiredArgsConstructor: final로 선언된 필드를 위한 생성자를 자동으로 만들어줍니다. (의존성 주입)
@RequiredArgsConstructor
public class PostService {

    // Post 데이터를 처리하기 위해 PostRepository를 주입받습니다.
    private final PostRepository postRepository;
    // User 데이터를 조회하기 위해 UserRepository를 주입받습니다.
    private final UserRepository userRepository;

    /**
     * 게시글 생성 기능을 처리하는 메소드
     * @param userId 작성자 ID
     * @param requestDto 게시글 생성 요청 DTO (title, content, boardType)
     * @return 저장된 Post 객체
     */
    @Transactional
    public Post create(Long userId, PostCreateRequestDto requestDto) {
        
        // 1. userId를 사용하여 작성자(User) 정보를 데이터베이스에서 조회합니다.
        // 만약 해당 ID의 유저가 없으면, Exception을 발생시켜 작업을 중단합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. id=" + userId));

        // 2. DTO와 조회된 User 객체를 사용하여 새로운 Post 객체를 생성합니다.
        Post newPost = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .board_type(requestDto.getBoardType())
                .user(user) // 게시글에 작성자 정보를 연결합니다.
                .build();

        // 3. postRepository를 사용하여 생성된 Post 객체를 데이터베이스에 저장합니다.
        return postRepository.save(newPost);
    }

    /**
     * 게시글 단건 조회 기능을 처리하는 메소드
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 정보를 담은 DTO
     */
    // @Transactional(readOnly = true): 이 메소드는 데이터베이스를 읽기만 하는 작업이므로,
    // readOnly = true 옵션을 주면 성능이 향상됩니다.
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long postId) {
        
        // 1. postId를 사용하여 Post 객체를 데이터베이스에서 조회합니다.
        // 만약 해당 ID의 게시글이 없으면, Exception을 발생시켜 작업을 중단합니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        // 2. 조회된 Post 객체를 PostResponseDto로 변환하여 반환합니다.
        return new PostResponseDto(post);
    }

    /**
     * 모든 게시글 목록을 조회하는 기능을 처리하는 메소드
     * @return 게시글 정보 DTO를 담은 리스트
     */
    @Transactional(readOnly = true)
    public List<PostResponseDto> findAll() {
        
        // 1. postRepository를 사용하여 데이터베이스에 저장된 모든 Post 객체를 조회합니다.
        List<Post> posts = postRepository.findAll();

        // 2. 조회된 Post 객체 리스트를 PostResponseDto 리스트로 변환합니다.
        //    Java Stream API를 사용하여 각 Post 객체를 PostResponseDto로 매핑(변환)합니다.
        return posts.stream()
                .map(PostResponseDto::new) // 각 post 객체를 new PostResponseDto(post)로 변환
                .collect(Collectors.toList()); // 변환된 DTO들을 다시 리스트로 묶음
    }
    /**
     * 게시글 수정 기능을 처리하는 메소드
     * @param postId 수정할 게시글의 ID
     * @param requestDto 새로운 제목과 내용을 담은 DTO
     * @return 수정된 게시글의 ID
     */
    @Transactional // DB에 변경 사항을 반영해야 하므로 @Transactional을 붙입니다.
    public Long update(Long postId, PostUpdateRequestDto requestDto) {
        
        // 1. postId를 사용하여 수정할 Post 객체를 데이터베이스에서 조회합니다.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        // 2. 조회된 Post 객체의 내용을 새로운 내용으로 업데이트합니다.
        //    (이 update 메소드는 이전에 Post Entity 클래스 안에 직접 만들어 둔 것입니다.)
        post.update(requestDto.getTitle(), requestDto.getContent());

        // 3. @Transactional 어노테이션 덕분에, 이 메소드가 끝나면 JPA가 변경된 내용을 감지하여
        //    자동으로 UPDATE 쿼리를 실행해줍니다. 따라서 repository.save()를 또 호출할 필요가 없습니다.
        //    이것을 '더티 체킹(Dirty Checking)'이라고 합니다.

        return postId;
    }
    /**
     * 게시글 삭제 기능을 처리하는 메소드
     * @param postId 삭제할 게시글의 ID
     */
    @Transactional
    public void delete(Long postId) {
        
        // postRepository의 deleteById 메소드를 사용하여 ID에 해당하는 게시글을 삭제합니다.
        // JPA가 먼저 해당 ID의 게시글이 있는지 확인하고 삭제를 진행합니다.
        postRepository.deleteById(postId);
    }    
}