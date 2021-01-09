package com.mouritech.report.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.mouritech.report.entity.Employee;
import com.mouritech.report.repository.EmployeeRepository;


@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private EmployeeRepository repository;
	
	@Autowired
	private Environment env;

	@Value("${pdfFileName}")
	String pdfFileName;
	
	@Value("${excelFileName}")
	String excelFileName;
	
	@Value("${htmlFileName}")
	String htmlFileName;
	
	
		

	public byte[] exportReportWithData(String reportFormat,
			List<Employee> employees) throws JRException, IOException {
		

		// load file and compile it
		File file = ResourceUtils.getFile("classpath:employees.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file
				.getAbsolutePath());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
				employees);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "Mouri Tech");
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
				parameters, dataSource);
		
		byte[] bytes = null;
		String generatedFile="";
		if (reportFormat.equalsIgnoreCase("html")) {
			generateHtmlFile(jasperPrint);
			 bytes=Files.readAllBytes(Paths.get(htmlFileName));
			 generatedFile=htmlFileName;
		}
		if (reportFormat.equalsIgnoreCase("pdf")) {

			generatePdfFile(jasperPrint);
		
			 bytes=Files.readAllBytes(Paths.get(pdfFileName));
			 generatedFile=pdfFileName;
		}
		if (reportFormat.equalsIgnoreCase("excel")) {

			generateExcelFile(jasperPrint);
			 bytes=Files.readAllBytes(Paths.get(excelFileName));
			 generatedFile=excelFileName;

		}
		
		deleteGeneratedFile(generatedFile);
					
		return bytes;
		
	}

	private void generatePdfFile(JasperPrint jasperPrint) throws JRException {

		JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFileName);

	}
	
	private void generateHtmlFile(JasperPrint jasperPrint) throws JRException {

		JasperExportManager.exportReportToHtmlFile(jasperPrint,htmlFileName );

	}

	private void generateExcelFile(JasperPrint jasperPrint) throws JRException {

		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		File outputFile = new File(excelFileName);
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
				outputFile));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		configuration.setDetectCellType(true);// Set configuration as you like
												// it!!
		configuration.setCollapseRowSpan(false);
		exporter.setConfiguration(configuration);
		exporter.exportReport();

	}
	private void deleteGeneratedFile(String file) throws IOException{
		
		File generatedFile=new File(file);
		
		if(generatedFile.exists()){
			generatedFile.delete();
		}
	}

	@Override
	public List<Employee> employeesList() {
		List<Employee> empLst = repository.findAll();
		return empLst;
	}

	@Override
	public List<Employee> employeesList(String designation) {
		List<Employee> empLst = repository.findByDesignation(designation);
		return empLst;
	}


}
