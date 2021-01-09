package com.mouritech.report.service;

import java.io.IOException;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import com.mouritech.report.entity.Employee;


public interface ReportService {
	
	public byte[] exportReportWithData(String reportFormat,List<Employee> employees) throws JRException, IOException;
	
	public List<Employee> employeesList();
	
	public List<Employee> employeesList(String designation);
	
}
