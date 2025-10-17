package com.ddu.mini.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.mini.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
