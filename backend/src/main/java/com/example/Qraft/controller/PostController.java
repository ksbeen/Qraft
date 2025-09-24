package com.example.Qraft.controller;

import com.example.Qraft.dto.PostCreateRequestDto;
import com.example.Qraft.dto.PostResponseDto;
import com.example.Qraft.dto.PostUpdateRequestDto;
import com.example.Qraft.entity.Post;
import com.example.Qraft.entity.PostReaction;
import com.example.Qraft.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/{id}/views")
    public ResponseEntity<Void> increaseViews(@PathVariable Long id) {
        postService.increaseViews(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostCreateRequestDto requestDto, Authentication auth) {
        String userEmail = auth.getName();
        Post createdPost = postService.create(userEmail, requestDto);
        return ResponseEntity.ok(createdPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePost(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto) {
        Long updatedPostId = postService.update(id, requestDto);
        return ResponseEntity.ok(updatedPostId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/recommend")
    public ResponseEntity<PostResponseDto> recommendPost(@PathVariable Long id, Authentication auth) {
        String userEmail = auth.getName();
        postService.reactToPost(id, PostReaction.ReactionType.RECOMMEND, userEmail);
        PostResponseDto updatedPost = postService.getPostWithReactions(id);
        return ResponseEntity.ok(updatedPost);
    }

    @PostMapping("/{id}/oppose")
    public ResponseEntity<PostResponseDto> opposePost(@PathVariable Long id, Authentication auth) {
        String userEmail = auth.getName();
        postService.reactToPost(id, PostReaction.ReactionType.OPPOSE, userEmail);
        PostResponseDto updatedPost = postService.getPostWithReactions(id);
        return ResponseEntity.ok(updatedPost);
    }
}
