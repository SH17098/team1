package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer>{

	List<Report> findByUserCode(int userCode);

	List<Report> findByOrderByCodeAsc();

}
