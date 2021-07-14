package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer>{
   long countByTweetCode(Integer tweetCode);
   Optional<Like> findByUserCodeAndTweetCode(int userCode, int tweetCode);
   Optional<Like> findByOrderByTweetCodeAsc();
}
