package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer>{
   List<Reply> findByTweetCode(int tweetCode);
}
