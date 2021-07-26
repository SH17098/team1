package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer>{
 List<Tweet> findByOrderByCodeAsc();
 List<Tweet> findByOrderByCodeDesc();
void saveAndFlush(String tweet);
}
