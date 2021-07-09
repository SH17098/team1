package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
 List<Comment> findByBookCode(int bookCode); //本の種類に合わせて検索
// Optional<Comment> findByBookCode(int bookCode); //本の種類に合わせて検索
}
