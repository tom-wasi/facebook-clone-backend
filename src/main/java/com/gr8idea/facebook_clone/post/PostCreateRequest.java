package com.gr8idea.facebook_clone.post;

public record PostCreateRequest (
        String userId,
        String content
)
{ }
