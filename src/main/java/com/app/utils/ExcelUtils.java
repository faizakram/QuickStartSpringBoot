package com.app.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ExcelUtils {
    private ExcelUtils() {
    }

    /**
     * Get All Sheet Name
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static Map<String, XSSFSheet> getSheetsName(InputStream inputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        Map<String, XSSFSheet> sheets = new HashMap<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheets.put(workbook.getSheetAt(i).getSheetName(), workbook.getSheetAt(i));
        }
        return sheets;
    }

    /**
     * Read Sheet
     *
     * @param xssfSheet
     * @return
     */
    public static Map<Integer, List<Object>> readSheet(XSSFSheet xssfSheet) {
        Map<Integer, List<Object>> data = new TreeMap<>();
        for (Row row : xssfSheet) {
            List<Object> list = new ArrayList<>();
            for (Cell cell : row) {
                list.add(cell);
            }
            data.put(row.getRowNum(), list);
        }
        return data;
    }

    /**
     * Read Sheet Array
     *
     * @param xssfSheet
     * @return
     */
    public static List<String[]> readSheetArray(XSSFSheet xssfSheet) {
        List<String[]> list = new ArrayList<>();
        for (Row row : xssfSheet) {
            if (row.getLastCellNum() < 0 || row.getCell(0).toString().isEmpty())
                break;
            String[] cellArray = new String[row.getLastCellNum()];
            for (Cell cell : row) {
                cellArray[cell.getColumnIndex()] = cell.toString();
            }
            list.add(cellArray);
        }
        return list;
    }

    public static XSSFWorkbook getXSSFWorkbookTemplate(String migrationPath) throws IOException {

        return new XSSFWorkbook(new FileInputStream(migrationPath));
    }

    public static CellStyle setCellSytle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLUE.getIndex());
        style.setBorderTop(BorderStyle.MEDIUM_DASHED);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }

    public static CellStyle getStyleBoldFont(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setItalic(true);
        style.setFont(font);
        return style;
    }

    public static CellStyle getStyleColorGREY25(XSSFWorkbook workbook, boolean isBold) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if (isBold) {
            Font font = workbook.createFont();
            font.setBold(true);
            font.setItalic(true);
            style.setFont(font);
        }
        return style;
    }

    public static CellStyle getStyleColorGREY50(XSSFWorkbook workbook, boolean isBold) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if (isBold) {
            Font font = workbook.createFont();
            font.setBold(true);
            font.setItalic(true);
            style.setFont(font);
        }

        return style;
    }

    /**
     * @param workbook
     * @param sourceWorksheet
     * @param sourceRowNum
     * @param destinationWorksheet
     * @param destinationRowNum
     */
    public static void copyFromSourceToDestinationRow(XSSFWorkbook workbook, XSSFSheet sourceWorksheet,
                                                      int sourceRowNum, XSSFSheet destinationWorksheet, int destinationRowNum) {
        // Get the source / new row
        XSSFRow sourceRow = sourceWorksheet.getRow(sourceRowNum);
        XSSFRow newRow = destinationWorksheet.createRow(destinationRowNum);
        // store the cell styles in map as cache to reuse
        Map<Short, XSSFCellStyle> styleCache = new HashMap<>();
        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            XSSFCell oldCell = sourceRow.getCell(i);
            XSSFCell newCell = newRow.createCell(i);
            // If the old cell is null jump to next cell
            if (oldCell == null) {
                continue;
            }

            // Copy style from old cell and apply to new cell
            XSSFCellStyle newCellStyle = styleCache.computeIfAbsent(oldCell.getCellStyle().getIndex(), index -> {
                XSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle.cloneStyleFrom(oldCell.getCellStyle());
                return cellStyle;
            });
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case BLANK:// Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
                default:
                    break;
            }
        }

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < sourceWorksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = sourceWorksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
                        cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
                destinationWorksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }
}
