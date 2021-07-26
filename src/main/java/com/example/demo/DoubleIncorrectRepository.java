package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoubleIncorrectRepository extends JpaRepository<DoubleIncorrects, Integer>{
List<DoubleIncorrects> findByUserCode(int userCode);
Optional<DoubleIncorrects> findByUserCodeAndTestCode(int userCode, int testCode);
void deleteByTestCode(int testCode);
Optional<DoubleIncorrects> findByTestCode(int deleteCode);
}
