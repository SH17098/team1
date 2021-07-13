package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{
	Optional<Book> findByName(String name); //名前で検索
	List<Book> findByOrderByCodeAsc(); //順に表示
	List<Book> findByOrderByRateDesc(); //評価の高い順に表示
	List<Book> findByOrderByCommentDesc(); //評価の高い順に表示
}
