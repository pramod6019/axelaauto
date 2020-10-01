package axela.customer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cloudify.connect.Connect;

/**
 *
 * @author SAMEEK
 */
public class Xlsx extends Connect{

    public int getNumberOfColumn(String fileName, int sheetIndex) throws FileNotFoundException, IOException {
//        HttpSession session = request.getSession(true);
//            comp_id = CNumeric(GetSession("comp_id", request));
//        
        File inputFile = null;
        FileInputStream fis = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        XSSFRow row = null;
        int lastRowNum = 0;
        int lastCellNum = 0;


        // Open the workbook
        inputFile = new File(fileName);
        fis = new FileInputStream(inputFile);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheetAt(sheetIndex);
        lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i < lastRowNum; i++) {

            row = sheet.getRow(i);
            if (row != null) {
                if (row.getLastCellNum() > lastCellNum) {
                    lastCellNum = row.getLastCellNum();
                }
            }
        }

        return lastCellNum;
    }

    public int getNumberOfRow(String fileName, int sheetIndex) throws FileNotFoundException, IOException {
        File inputFile = null;
        FileInputStream fis = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        int lastRowNum = 0;

        // Open the workbook
        inputFile = new File(fileName);
        fis = new FileInputStream(inputFile);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheetAt(sheetIndex);
        lastRowNum = sheet.getLastRowNum();
        return lastRowNum;
    }

    public String[] getSheetName(String fileName) throws FileNotFoundException, IOException {
        int totalsheet = 0;
        int i = 0;
        String[] sheetName = null;
        File inputFile = null;
        FileInputStream fis = null;
        XSSFWorkbook workbook = null;

        // Open the workbook
        inputFile = new File(fileName);
        fis = new FileInputStream(inputFile);
        workbook = new XSSFWorkbook(fis);
        totalsheet = workbook.getNumberOfSheets();
        sheetName = new String[totalsheet];
        while (i < totalsheet) {
            sheetName[i] = workbook.getSheetName(i);
            i++;
        }

        return sheetName;
    }

    public int getNumberOfSheet(String fileName) throws FileNotFoundException, IOException {
        int totalsheet = 0;
        File inputFile = null;
        FileInputStream fis = null;
        XSSFWorkbook workbook = null;
        XSSFSheet sheet = null;
        int lastRowNum = 0;

        // Open the workbook
        inputFile = new File(fileName);
        fis = new FileInputStream(inputFile);
        workbook = new XSSFWorkbook(fis);
        totalsheet = workbook.getNumberOfSheets();
        return totalsheet;
    }

    public String[][] getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
        String[][] data = null;
        int i = 0;
        int j = 0;
        Cell cell = null;
        long emptyrowcount = 0;
        InputStream inputStream = new FileInputStream(
                fileName);
        // Create a workbook object.
        Workbook wb = WorkbookFactory.create(inputStream);
        wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
        Sheet sheet = wb.getSheetAt(sheetIndex);
        // Iterate over all the row and cells
        int noOfColumns = getNumberOfColumn(fileName, sheetIndex);
        SOP("noOfColumns::" + noOfColumns);
        int noOfRows = getNumberOfRow(fileName, sheetIndex) + 1;
        SOP("noOfRows::" + noOfRows);
        data = new String[noOfRows][noOfColumns];
        for (int k = 0; k < noOfRows; k++) {
            Row row = sheet.getRow(k);
            if (row == null) {
            } else {
                j = 0;
                for (int l = 0; l < noOfColumns; l++) {
                    // Cell cell = cit.next();
                    cell = row.getCell(j);


                    if (cell.getCellType() == cell.CELL_TYPE_BLANK) {
                        cell = row.getCell(j, Row.CREATE_NULL_AS_BLANK);
                    }

                    data[i][j] = getCellValueAsString(cell);
                    j++;

                }
                i++;

            }
        }

        return data;
    }

    /**
     * This method for the type of data in the cell, extracts the data and
     * returns it as a string.
     */
    public static String getCellValueAsString(Cell cell) {
        String strCellValue = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    strCellValue = cell.toString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "dd/MM/yyyy");
                        strCellValue = dateFormat.format(cell.getDateCellValue());
                    } else {
                        Double value = cell.getNumericCellValue();
                        Long longValue = value.longValue();
                        strCellValue = new String(longValue.toString());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    strCellValue = new String(new Boolean(
                            cell.getBooleanCellValue()).toString());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    strCellValue = "null";
                    break;

            }
        }

        return strCellValue;
    }

    public static void main(String s[]) {
        try {
            String xlsxfile = "D:\\exe2\\murali\\EmployeeDetails.xlsx";
            Xlsx readXLSxFile = new Xlsx();
            String[][] sheetData = readXLSxFile.getSheetData(xlsxfile, 0);

            int columnLength = 0;
            columnLength = readXLSxFile.getNumberOfColumn(xlsxfile, 0);
            int rowLength = 0;
            rowLength = readXLSxFile.getNumberOfRow(xlsxfile, 0);


            int h = 0;
            int j = 0;
            while (j < rowLength) {
                h = 0;
                while (h < columnLength) {
                  //  System.out.print("" + sheetData[j][h]);
                    h++;
                }
//                SOP("");
                j++;
            }

        } catch (InvalidFormatException ex) {
            Logger.getLogger(Xlsx.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Xlsx.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Xlsx.class.getName()).log(Level.SEVERE, null, ex);


        }
    }
}
