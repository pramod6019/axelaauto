package cloudify.connect;
//Murali 6th sep

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;

public class ExportToHTML {
	Connect ct = new Connect();
	public void Export(HttpServletRequest request, HttpServletResponse response, CachedRowSet crs, String filename, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			String CountColumn = "";
			int count = 1;
			response.setContentType("text/html");
			// response.setHeader(" Content-Disposition", "attachment; filename=" + filename + "");
			PrintWriter out = response.getWriter();
			Str.append("<html>");
			Str.append("<HEAD>");
			// Str.append("<LINK REL=\"STYLESHEET\" TYPE=\"text/css\" HREF=\"http://localhost:8084/axela/Library/print-style.css\">");
			Str.append("<style type=\"text/css\">\n");
			String fileurl = request.getSession().getServletContext().getRealPath("/Library/print-style.css");
			// SOPError(fileurl);
			String stylez = new Scanner(new File(fileurl)).useDelimiter("line.seperator").next();
			// SOPError(stylez);
			Str.append(stylez);
			Str.append("\n</style>\n");

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
				// System.out.println(crs.next());
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td align=center valign=top >");
					Str.append(count + "");
					Str.append("</td>");
					for (int j = 1; j <= Integer.parseInt(CountColumn); j++) {
						Str.append("<td align=left valign=top>");
						if (!crs.getString(j).equals("")) {
							Str.append(new Connect().unescapehtml(crs.getString(j)));
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
			// ct.SOP(Str.toString());
			out.println(Str.toString());
			out.close();
		} catch (Exception ex) {
			ct.SOPError("Axelaauto== " + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

}
