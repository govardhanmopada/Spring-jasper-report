package com.mouritech.report;

import java.io.IOException;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mouritech.report.entity.Employee;
import com.mouritech.report.repository.EmployeeRepository;
import com.mouritech.report.service.ReportService;

/**
 * 
 * @author GovardhanaM
 *
 *         This is controller class.
 */

@RestController
@CrossOrigin(origins = "*")
public class Controller {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private ReportService service;

	/**
	 * Fetch Employees list end-point
	 */

	@GetMapping("/getEmployees")
	public List<Employee> getEmployees() {

		List<Employee> empLst = service.employeesList();
		return empLst;
	}

	/**
	 * Fetch Employee by Designation end-point
	 * 
	 * @param designation
	 */
	@GetMapping("/getEmployees/{designation}")
	public List<Employee> getEmployeesByDesig(@PathVariable String designation) {

		List<Employee> empLst = service.employeesList(designation);
		return empLst;
	}

	
	/**
	 * Generate report using jasper end-point
	 * 
	 * @param format
	 * @throws JRException
	 */
	
	@PostMapping("/reportWithData/{format}")
	public byte[] generateReportWIthData(@PathVariable String format,
			@RequestBody List<Employee> employeesList) throws JRException,
			IOException {
		return service.exportReportWithData(format, employeesList);
	}

}
