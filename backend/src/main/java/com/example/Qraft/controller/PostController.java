package com.example.Qraft.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.Qraft.dto.PostCreateRequestDto;
import com.example.Qraft.service.PostService;
import com.example.Qraft.dto.PostResponseDto;
import com.example.Qraft.dto.PostUpdateRequestDto;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
// 이 컨트롤러의 모든 API 주소는 '/api/posts'로 시작합니다.
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    // PostService의 의존성을 주입받습니다.
    private final PostService postService;

    // @PostMapping: HTTP POST 요청을 '/api/posts' 경로로 매핑합니다.
    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostCreateRequestDto requestDto) {

        // SecurityContextHolder에서 직접 현재 사용자의 인증 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증 정보에서 사용자의 이메일을 가져옵니다.
        String userEmail = authentication.getName();
        // DTO에서 받은 데이터를 사용하여 PostService의 create 메소드를 호출합니다.
        postService.create(userEmail, requestDto);

        // 게시글 생성이 성공하면, 성공 메시지를 담은 응답을 반환합니다.
        return ResponseEntity.ok("게시글이 성공적으로 등록되었습니다.");
    }

    /**
     * 게시글 단건 조회 API
     * @param postId URL 경로에서 추출한 게시글 ID
     * @return 조회된 게시글 정보를 담은 DTO
     */
    // @GetMapping("/{postId}"): HTTP GET 요청을 처리하는 메소드임을 나타냅니다.
    // URL 경로의 일부인 {postId} 값을 파라미터로 받기 위해 사용합니다.
    // 최종 URL은 '/api/posts/{게시글 ID}'가 됩니다. (예: /api/posts/1)
    @GetMapping("/{postId}")
    // @PathVariable: URL 경로에 포함된 변수({postId})의 값을 메소드의 파라미터(Long postId)로 매핑해줍니다.
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        
        // PostService의 findById 메소드를 호출하여 게시글 정보를 조회합니다.
        PostResponseDto responseDto = postService.findById(postId);

        // 조회된 DTO를 HTTP 상태 코드 200(OK)와 함께 응답 본문에 담아 반환합니다.
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 게시글 전체 목록 조회 API
     * @return 게시글 정보 DTO 리스트
     */
    // @GetMapping: HTTP GET 요청을 처리합니다. 경로가 없으므로 '/api/posts'로 매핑됩니다.
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        
        // PostService의 findAll 메소드를 호출하여 모든 게시글 정보를 조회합니다.
        List<PostResponseDto> responseDtos = postService.findAll();

        // 조회된 DTO 리스트를 HTTP 상태 코드 200(OK)와 함께 응답 본문에 담아 반환합니다.
        return ResponseEntity.ok(responseDtos);
    }
    /**
     * 게시글 수정 API
     * @param postId URL 경로에서 추출한 수정할 게시글의 ID
     * @param requestDto 요청 본문에 담긴 새로운 제목과 내용
     * @return 수정된 게시글의 ID
     */
    // @PutMapping("/{postId}"): HTTP PUT 요청을 처리하며, 경로의 {postId}를 변수로 받습니다.
    @PutMapping("/{postId}")
    public ResponseEntity<Long> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequestDto requestDto) {
        
        // PostService의 update 메소드를 호출하여 게시글을 수정합니다.
        Long updatedPostId = postService.update(postId, requestDto);

        // 수정된 게시글의 ID와 HTTP 상태 코드 200(OK)를 응답으로 반환합니다.
        return ResponseEntity.ok(updatedPostId);
    }
    /**
     * 게시글 삭제 API
     * @param postId URL 경로에서 추출한 삭제할 게시글의 ID
     * @return 성공 메시지
     */
    // @DeleteMapping("/{postId}"): HTTP DELETE 요청을 처리하며, 경로의 {postId}를 변수로 받습니다.
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        
        // PostService의 delete 메소드를 호출하여 게시글을 삭제합니다.
        postService.delete(postId);

        // 삭제가 성공하면, 성공 메시지와 HTTP 상태 코드 200(OK)를 응답으로 반환합니다.
        return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
    }
}