package com.mouritech.report.repository;

import java.util.List;

import com.mouritech.report.entity.Employee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
	
	public List<Employee> findByDesignation(String designation);
}
