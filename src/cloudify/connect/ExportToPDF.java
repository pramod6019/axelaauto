package cloudify.connect;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class ExportToPDF extends PageSize {
	Connect ct = new Connect();
	public void Export(HttpServletRequest request,
			HttpServletResponse response, CachedRowSet crs, String filename,
			String size, String comp_id) throws IOException {
		StringBuilder Str = new StringBuilder();
		try {
			// size = "A1";
			String CountColumn = "";
			int count = 1;
			response.setContentType("application/pdf");
			Str.append("<html>");
			Str.append("<HEAD>");
			Str.append("</HEAD>");
			Str.append("<body>");
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=listtable >\n");
				Str.append("<tr>");
				RowSetMetaData rsmd = (RowSetMetaData) crs.getMetaData();
				CountColumn = Integer.toString(rsmd.getColumnCount());
				Str.append("<th>#</th>");
				for (int k = 1; k < Integer.parseInt(CountColumn) + 1; k++) {
					Str.append("<th nowrap>");
					Str.append(rsmd.getColumnLabel(k));
					Str.append("</th>");
				}
				Str.append("</tr>\n");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td>");
					Str.append(count + "");
					Str.append("</td>");
					for (int j = 1; j <= Integer.parseInt(CountColumn); j++) {
						Str.append("<td align=left >");
						if (!crs.getString(j).equals("")) {
							Str.append(new Connect().unescapehtml(crs
									.getString(j)));
						} else {
							Str.append("&nbsp;");
						}
						Str.append("</td>");
					}
					Str.append("</tr>\n");
					count++;
				}
				Str.append("</table>");
			} else {
				Str.append("No Records Found");
			}
			Str.append("</body>");
			Str.append("</html>");
			Document document = new Document();
			if (size.equals("A0")) {
				document.setPageSize(PageSize.A0.rotate());
			} else if (size.equals("A1")) {
				document.setPageSize(PageSize.A1.rotate());
			} else if (size.equals("A2")) {
				document.setPageSize(PageSize.A2.rotate());
			} else if (size.equals("A3")) {
				document.setPageSize(PageSize.A3.rotate());
			} else {
				document.setPageSize(PageSize.A4.rotate());
			}
			PdfWriter pdfwriter = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();
			XMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, new StringReader(Str.toString()));
			document.close();
		} catch (Exception ex) {
			ct.SOPError("AxelaAuto===" + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}
}
