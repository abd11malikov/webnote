package com.otabek.blog.security;

import org.springframework.stereotype.Component;
import com.otabek.blog.repository.PostRepository;
import org.springframework.security.core.Authentication;

@Component("postSecurity")
public class PostSecurity {
    
    private final PostRepository postRepository;

    public PostSecurity(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public boolean isOwner(Long postId, Authentication authentication) {
        return postRepository.findById(postId)
                .map(post -> post.getAuthor().getUsername().equals(authentication.getName()))
                .orElse(false);
    }
}