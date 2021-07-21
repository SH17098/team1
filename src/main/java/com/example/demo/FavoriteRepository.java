package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer>{
  Optional<Favorite> findByUserCodeAndFlashcardCode(int userCode, int flashcardCode);
  List<Favorite> findByUserCode(int userCode);
void deleteByFlashcardCode(int flashcardCode);
}
