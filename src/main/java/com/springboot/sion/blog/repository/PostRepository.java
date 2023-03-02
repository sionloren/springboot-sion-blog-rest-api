package com.springboot.sion.blog.repository;

import com.springboot.sion.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
