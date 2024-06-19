package com.gr8idea.facebook_clone.post;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public String createPost(@RequestBody PostCreateRequest request) {
        postService.createPost(request);
        return "Post created successfully";
    }

    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable String postId, @RequestParam("userId") String userId) {
        postService.likePost(postId, userId);
        return "Post liked successfully";
    }

    @PostMapping("/{postId}/comment")
    public String commentOnPost(@PathVariable String postId, @RequestBody CommentCreateRequest request) {
        postService.commentOnPost(postId, request);
        return "Comment added successfully";
    }
}
