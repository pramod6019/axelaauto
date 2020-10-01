package cloudify.connect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportToXLSX {
	Connect ct = new Connect();
	public String emp_id = "0", export_id = "0";
	public PreparedStatement psmt = null;
	public CachedRowSet crs = null;
	public Statement stmt = null;
	public Connection conn = null;
	public void Export(HttpServletRequest request, HttpServletResponse response, CachedRowSet crs, String filename, String comp_id) {

		try {
			HttpSession session = request.getSession(true);
			emp_id = (session.getAttribute("emp_id")).toString();

			Row row = null;
			Cell cell = null;
			int columncount = 0;
			byte b = 1;
			String filepath = "";
			filepath = ct.TemplatePath("1000") + "template.xlsx";
			FileInputStream inputStream = new FileInputStream(filepath);

			XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
			inputStream.close();

			SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
			wb.setCompressTempFiles(true);

			CellStyle cs = wb.createCellStyle();
			Font font = wb.createFont();
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			font.setUnderline(b);
			cs.setFont(font);

			CellStyle cs1 = wb.createCellStyle();
			cs1.setAlignment(CellStyle.ALIGN_RIGHT);

			SXSSFSheet sh = (SXSSFSheet) wb.getSheetAt(0);
			sh.setRandomAccessWindowSize(100);// keep 100 rows in memory,
												// exceeding rows will be
												// flushed to disk

			RowSetMetaData rsmd = (RowSetMetaData) crs.getMetaData();
			columncount = rsmd.getColumnCount();

			row = sh.createRow(0);
			cell = row.createCell(0);
			cell.setCellValue("#");
			cell.setCellStyle(cs);
			int x = 1;
			for (int k = 1; k < columncount + 1; k++) {
				cell = row.createCell(k);
				cell.setCellValue(rsmd.getColumnLabel(k));
				cell.setCellStyle(cs);
			}
			while (crs.next()) {
				row = sh.createRow(x);
				cell = row.createCell(0);
				cell.setCellValue(x);
				for (int j = 1; j <= columncount; j++) {
					cell = row.createCell(j);
					if (rsmd.getColumnTypeName(j).equals("BIGINT") || rsmd.getColumnTypeName(j).equals("DOUBLE")) {
						cell.setCellValue(new Double(crs.getString(j)));
					} else if (rsmd.getColumnTypeName(j).equals("INT") || rsmd.getColumnTypeName(j).equals("INT UNSIGNED") || rsmd.getColumnTypeName(j).equals("DECIMAL")) {
						cell.setCellValue(new Double(crs.getString(j)));
					} else {
						cell.setCellValue(new Connect().unescapehtml(crs.getString(j)));
					}
				}
				x++;
			}
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xlsx");
			filepath = ct.CachePath(comp_id);
			File f = new File(filepath);
			if (!f.exists()) {
				f.mkdirs();
			}
			filepath = ct.CachePath(comp_id) + filename + ".xlsx";
			// System.out.println("cache filepath-------" + filepath);

			FileOutputStream out = new FileOutputStream(filepath);
			wb.write(out);

			// Save file for export
			if (!emp_id.equals("1")) {
				System.out.println("coming..");
				conn = null;
				conn = ct.connectDB();
				String StrSql = "INSERT INTO " + ct.compdb(comp_id) + "axela_emp_export"
						+ "(export_emp_id, export_value, export_data , export_time"
						+ ")"
						+ "values"
						+ " (?,?,?,?)";
				java.sql.PreparedStatement psmt = conn.prepareStatement(StrSql);
				psmt.setString(1, emp_id);
				psmt.setString(2, filename + ".xlsx");
				if (inputStream != null) {
					psmt.setBlob(3, new FileInputStream(filepath));
				}
				psmt.setString(4, ct.ToLongDate(ct.kknow()));
				psmt.executeUpdate();
			}
			// End Save file for export

			out.close();
			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				response.getOutputStream().write(i);
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
			fileInputStream.close();
			return;
		} catch (Exception ex) {
			System.out.println("Axelaauto===" + this.getClass().getName());
			System.out.println("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			try {
				if (crs != null) {
					crs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				System.out.println("Axelaauto===" + this.getClass().getName());
				System.out.println("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
			// The connection is returned to the Broker

		}
	}

}
