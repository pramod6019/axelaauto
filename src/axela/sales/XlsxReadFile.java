package axela.sales;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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

/**
 *
 * @author SAMEEK
 */
public class XlsxReadFile {

	public int getNumberOfColumn(String fileName, int sheetIndex) throws FileNotFoundException, IOException {
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
		int emptyrowcount = 0;
		InputStream inputStream = new FileInputStream(fileName);
		// Create a workbook object.
		Workbook wb = WorkbookFactory.create(inputStream);
		wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
		Sheet sheet = wb.getSheetAt(sheetIndex);
		// Iterate over all the row and cells
		int noOfColumns = getNumberOfColumn(fileName, sheetIndex);
		// SOP("noOfColumns::" + noOfColumns);
		int noOfRows = getNumberOfRow(fileName, sheetIndex) + 1;
		data = new String[noOfRows][noOfColumns];
		for (int k = 0; k < noOfRows; k++) {
			// System.out.println(k + "-----");

			emptyrowcount = 0;
			Row row = sheet.getRow(k);

			if (row == null || row.getZeroHeight() == true) {
				// System.out.println(k + "---" + row);
				row.setZeroHeight(true);
			}
			if (row.getZeroHeight() == false) {
				j = 0;
				for (int l = 0; l < noOfColumns; l++) {
					// Cell cell = cit.next();
					cell = row.getCell(j);
					if (cell.getCellType() == cell.CELL_TYPE_BLANK) {
						cell = row.getCell(j, Row.CREATE_NULL_AS_BLANK);
					}
					if (cell.getCellType() == cell.CELL_TYPE_NUMERIC
							&& !getCellValueAsString(cell).contains("/")
							&& getCellValueAsString(cell).contains(".")) {
						data[i][j] = Double.parseDouble(getCellValueAsString(cell)) + "";
					} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC
							&& !getCellValueAsString(cell).contains("/")
							&& !getCellValueAsString(cell).contains(".")) {
						data[i][j] = Long.parseLong(getCellValueAsString(cell)) + "";
					} else {
						data[i][j] = getCellValueAsString(cell);
					}
					if (data[i][j] == null || data[i][j].equals("null")) {
						data[i][j] = "";
					}
					if (data[i][j].equals("")) {
						emptyrowcount++;
					}
					if (emptyrowcount == noOfColumns) {
						// noOfRows = k;
					}
					j++;

				}
				i++;

			}
		}
		return data;
	}
	/**
	 * This method for the type of data in the cell, extracts the data and returns it as a string.
	 */
	public static String getCellValueAsString(Cell cell) {
		String strCellValue = null;
		if (cell != null) {
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING :
					strCellValue = cell.toString();
					break;
				case Cell.CELL_TYPE_NUMERIC :
					if (DateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						strCellValue = dateFormat.format(cell.getDateCellValue());
					} else {
						DecimalFormat df = new DecimalFormat("0.00");
						cell.setCellType(cell.CELL_TYPE_STRING);
						Double value = Double.parseDouble(cell.getStringCellValue());
						value = Double.parseDouble(df.format(value));
						Long longValue = value.longValue();
						strCellValue = new String(longValue.toString());
						if (strCellValue.equals("0")) {

							cell.setCellType(cell.CELL_TYPE_STRING);
							value = Double.parseDouble(cell.getStringCellValue());
							value = Double.parseDouble(df.format(value));
							longValue = value.longValue();
							strCellValue = new String(value.toString());
						}
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN :
					strCellValue = new String(new Boolean(
							cell.getBooleanCellValue()).toString());
					break;
				case Cell.CELL_TYPE_BLANK :
					strCellValue = "null";
					break;

			}
		}

		return strCellValue;
	}

	public static void main(String s[]) {
		try {
			String xlsxfile = "D:\\exe2\\murali\\EmployeeDetails.xlsx";
			XlsxReadFile readXLSxFile = new XlsxReadFile();
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
					// System.out.print("--" + sheetData[j][h]);
					h++;
				}
				// SOP("");
				j++;
			}

		} catch (InvalidFormatException ex) {
			Logger.getLogger(XlsxReadFile.class.getName()).log(Level.SEVERE, null, ex);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(XlsxReadFile.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(XlsxReadFile.class.getName()).log(Level.SEVERE, null, ex);

		}
	}
}
