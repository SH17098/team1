package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncorrectRepository extends JpaRepository<Incorrect, Integer>{
List<Incorrect> findByUserCode(int userCode);
List<Incorrect> findByUserCodeAndTestCode(int userCode, int testCode);
void deleteByTestCode(int testCode);
Optional<Incorrect> findByTestCode(int deleteCode);
}
