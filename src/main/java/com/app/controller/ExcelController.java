package com.app.controller;

import com.app.Download;
import com.app.service.ExcelService;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    @Value("${spring.resources.paths}")
    private String path;
    @GetMapping("download")
    public StreamingResponseBody downloadResource(
            HttpServletResponse httpResponse) throws IOException {
        Download download = excelService.downloadExcelFile();
        httpResponse.setContentType("application/octet-stream");
        httpResponse.setHeader("Content-Disposition",
                String.format("inline; filename=\"%s\"", String.format("Student-Details-%s.xlsx", LocalDateTime.now().getMinute())));
        return outputStream -> {
            outputStream.write(download.getBytes());
            outputStream.flush();
        };
    }

    @GetMapping("download-pdf")
    public StreamingResponseBody downloadPDFResource(
            HttpServletResponse httpResponse) throws IOException {
        Download download = excelService.downloadPDFFile();
        httpResponse.setContentType("application/octet-stream");
        httpResponse.setHeader("Content-Disposition",
                String.format("inline; filename=\"%s\"", String.format("Student-Details-%s.pdf", LocalDateTime.now().getMinute())));
        return outputStream -> {
            outputStream.write(download.getBytes());
            outputStream.flush();
        };
    }

    @GetMapping("download-pdf2")
    public StreamingResponseBody downloadPDFResource2(
            HttpServletResponse httpResponse) throws IOException {
        Download download = excelService.downloadPDFFile2();
        httpResponse.setContentType("application/octet-stream");
        httpResponse.setHeader("Content-Disposition",
                String.format("inline; filename=\"%s\"", String.format("Student-Details-%s.pdf", LocalDateTime.now().getMinute())));
        return outputStream -> {
            outputStream.write(download.getBytes());
            outputStream.flush();
        };
    }

    @GetMapping("download-pdf3")
    public StreamingResponseBody downloadPDFResource3(
            HttpServletResponse httpResponse) throws Exception {
        //Download download = excelService.downloadPDFFile2();
        httpResponse.setContentType("application/octet-stream");
        httpResponse.setHeader("Content-Disposition",
                String.format("inline; filename=\"%s\"", String.format("Student-Details-%s.pdf", LocalDateTime.now().getMinute())));
        return outputStream -> {
            try {
                outputStream.write(convertExcelToPDFStream(path+"file.xlsx"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            outputStream.flush();
        };
    }



    public byte[] convertExcelToPDFStream(String excelFilePath) throws Exception {
        // Load the Excel file
        Workbook workbook = new Workbook(excelFilePath);

        // Configure PDF save options
        PdfSaveOptions options = new PdfSaveOptions();
        options.setOnePagePerSheet(true); // Adjust this as needed

        // Create a ByteArrayOutputStream to hold the PDF data
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

        // Save the workbook to ByteArrayOutputStream as PDF
        workbook.save(pdfOutputStream, options);

        // Return the ByteArrayOutputStream containing the PDF
        return pdfOutputStream.toByteArray();
    }
}
