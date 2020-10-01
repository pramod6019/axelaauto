package cloudify.connect;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Xlsx {
	String CountColumn = "";
	int rowcount = 0;
	int x = 1;
	public void Export(HttpServletResponse response, CachedRowSet crs, String filename) throws IOException, SQLException {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();
		// int rowCount=0;
		// int count=0;
		RowSetMetaData rsmd = (RowSetMetaData) crs.getMetaData();
		CountColumn = Integer.toString(rsmd.getColumnCount());
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
		crs.last();
		rowcount = crs.getRow();
		crs.beforeFirst();
		XSSFRow headerRow = sheet.createRow((short) 1);
		XSSFRow dataRow = sheet.createRow((short) rowcount);

		for (int k = 1; k < Integer.parseInt(CountColumn) + 1; k++) {
			headerRow.createCell((short) k).setCellValue(rsmd.getColumnLabel(k));
		}

		while (crs.next()) {

			for (int i = x; i <= rowcount; i++) {
				sheet.createRow((short) i);
				// dataRow.createCell((short)0).setCellValue(1);
				for (int j = 1; j <= Integer.parseInt(CountColumn); j++) {
					dataRow.createCell((short) j).setCellValue(crs.getString(j));
					// SOPError("5");

				}

			}
			x++;

			// SOPError("5");
		}
		// File f = new File("D:\\exe2\\murali\\"+filename+".xlsx");
		// FileOutputStream fos = new FileOutputStream(f);
		// wb.write(fos);
	}
}
