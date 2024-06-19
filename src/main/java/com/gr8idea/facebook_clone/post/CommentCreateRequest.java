package com.gr8idea.facebook_clone.post;

public record CommentCreateRequest(
        String userId,
        String postId,
        String content
) {
}
