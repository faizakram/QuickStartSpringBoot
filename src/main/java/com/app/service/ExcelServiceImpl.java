package com.app.service;

import com.app.Download;
import com.app.dto.StudentDownload;
import com.app.utils.ExcelUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Value("${spring.resources.paths}")
    private String path;

    @Override
    public Download downloadExcelFile() throws IOException {
        Download download = new Download();
        download.setBytes(writeExcelToStream(getStudents()).toByteArray());
        return download;
    }

    @Override
    public Download downloadPDFFile() throws IOException {
        Download download = new Download();
        download.setBytes(writePDFToStream(getWorkBook(getStudents())).toByteArray());
        return download;
    }
    @Override
    public Download downloadPDFFile2() throws IOException {
        Download download = new Download();
        download.setBytes(writePDFToStream(getWorkBook()).toByteArray());
        return download;
    }

    private List<StudentDownload> getStudents() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("D:\\Compunnel\\QuickStartSpringBoot\\src\\main\\resources\\student.json"), new TypeReference<List<StudentDownload>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private ByteArrayOutputStream writeExcelToStream(List<StudentDownload> studentDownloads) throws IOException {
        Workbook workbook = getSheets(studentDownloads);  // Creates an Excel workbook
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);  // Write the workbook to the ByteArrayOutputStream
        } finally {
            workbook.close();  // Ensure the workbook is closed to free resources
            outputStream.close();  // Although not necessary, it's good practice to close the stream
        }

        return outputStream;  // Return the ByteArrayOutputStream containing the Excel data
    }


    private Workbook getWorkBook(List<StudentDownload> studentDownloads) throws IOException {
        Workbook workbook = getSheets(studentDownloads);
        workbook.close();  // Ensure the workbook is closed to free resources

        return workbook;  // Return the ByteArrayOutputStream containing the Excel data
    }

    private Workbook getWorkBook() throws IOException {
        Workbook workbook = ExcelUtils.getXSSFWorkbookTemplate(path + "file.xlsx");
        workbook.close();  // Ensure the workbook is closed to free resources
        return workbook;  // Return the ByteArrayOutputStream containing the Excel data
    }

    private static Workbook getSheets(List<StudentDownload> studentDownloads) {
        Workbook workbook = new XSSFWorkbook();  // Creates an Excel workbook
        Sheet sheet = workbook.createSheet("Students");  // Creates a sheet named "Students"
        // Creating the header row
        String[] headers = {"Regno", "Certificate No", "Name", "Guardian Name", "Address", "Mobile"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Filling data
        int rowNum = 1;
        for (StudentDownload download : studentDownloads) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(download.getRegNo());
            row.createCell(1).setCellValue(download.getCertificateNo());
            row.createCell(2).setCellValue(download.getName());
            row.createCell(3).setCellValue(download.getGuardianName());
            row.createCell(4).setCellValue(download.getAddress());
            row.createCell(5).setCellValue(download.getMobile());
        }

        // Auto-size columns to fit the contents
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }


    public ByteArrayOutputStream writePDFToStream(Workbook workbook) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Create a PdfWriter instance that writes to a ByteArrayOutputStream
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Assuming there's only one sheet in the Excel file
        Sheet sheet = workbook.getSheetAt(0);

        // Create a table with the same number of columns as the Excel sheet
        Table table = new Table(UnitValue.createPercentArray(sheet.getRow(0).getLastCellNum())).useAllAvailableWidth();

        // Read each row and cell from the Excel sheet
        for (Row row : sheet) {
            for (Cell cell : row) {
                // Get the cell's content as a string
                String text = cell.toString();
                // Add a cell to the PDF table with the text from the Excel cell
                table.addCell(new com.itextpdf.layout.element.Cell().add(new com.itextpdf.layout.element.Paragraph(text)));
            }
        }

        // Add the table to the document and close the document
        document.add(table);
        document.close();

        return byteArrayOutputStream;  // Return the ByteArrayOutputStream containing the PDF data
    }



}
