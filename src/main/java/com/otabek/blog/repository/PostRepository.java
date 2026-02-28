package com.otabek.blog.repository;

import com.otabek.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
}