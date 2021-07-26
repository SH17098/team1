package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//	List<User> findByUserIdAndPassword(String userId,String password);
	Optional<User> findByUserId(String userId);
	Optional<User> findByEmail(String email);
//	List<User> findByOrderByCodeAsc();

}
