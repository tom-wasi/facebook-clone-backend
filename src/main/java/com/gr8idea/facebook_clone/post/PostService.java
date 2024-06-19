package com.gr8idea.facebook_clone.post;

import com.gr8idea.facebook_clone.exception.PostNotFoundException;
import com.gr8idea.facebook_clone.user.AppUserService;
import com.gr8idea.facebook_clone.user.internal.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AppUserService appUserService;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public void createPost(PostCreateRequest request) {
        AppUser user = appUserService.getUser(request.userId());
        Post post = Post.builder()
                .user(user)
                .content(request.content())
                .createdAt(new Date())
                .build();
        postRepository.save(post);
    }

    public void likePost(String postId, String userId) {
        AppUser user = appUserService.getUser(userId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();
        likeRepository.save(like);
    }

    public void commentOnPost(String postId, CommentCreateRequest request) {
        AppUser user = appUserService.getUser(request.userId());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.content())
                .createdAt(new Date())
                .build();
        commentRepository.save(comment);
    }
}






