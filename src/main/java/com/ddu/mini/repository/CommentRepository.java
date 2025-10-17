package com.ddu.mini.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.mini.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
