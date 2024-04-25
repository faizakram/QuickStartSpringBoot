package com.app.service;

import com.app.Download;

import java.io.IOException;

public interface ExcelService {
    Download downloadExcelFile() throws IOException;

    Download downloadPDFFile() throws IOException;

    Download downloadPDFFile2() throws IOException;
}
