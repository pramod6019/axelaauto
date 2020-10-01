/* Ved Prakash (24th July 2013) */
package axela.sales;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class SalesOrder_Dash_Docs extends Connect {

	public String msg = "";
	public String all = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String QueryString = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String PageCurrents = "";
	public String PageNaviStr = "";
	public int PageCount = 10;
	public int PageCurrent = 0;
	public int recperpage = 0;
	public String RecCountDisplay = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String doc_id = "";
	public String so_id = "0";
	public String so_desc = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));

				StrSql = "SELECT so_id, emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS customer_exe"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " WHERE so_id = " + so_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY so_id"
						+ " ORDER BY so_id DESC";
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						so_id = crs.getString("so_id");
					}
					StrHTML = ListDocs(so_id, PageCurrents, recperpage, QueryString, comp_id);
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Sales Order!");
				}
				crs.close();

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND doc_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Documents";
					StrSearch += " AND doc_id > 0";
				} else if (!(doc_id.equals("0"))) {
					msg += "<br>Results for Doc ID = " + doc_id + "!";
					StrSearch += " AND doc_id = " + doc_id + "";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListDocs(String so_id, String PageCurrents, int recperpage, String QueryString, String comp_id) {
		StringBuilder Str = new StringBuilder();
		int TotalRecords;
		String CountSql;
		String SqlJoin;
		int StartRec;
		int EndRec;
		String PageURL;
		int PageListSize = 10;

		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		StrSql = "SELECT doc_id, doc_value, doc_title, doc_remarks, so_desc";

		CountSql = "SELECT COUNT(doc_id)";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_so_docs"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = doc_so_id"
				+ " WHERE 1 = 1"
				+ " AND doc_so_id = " + so_id + "";

		StrSql = StrSql + SqlJoin + StrSearch;
		CountSql = CountSql + SqlJoin + StrSearch;

		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		Str.append("<div class=\"container-fluid portlet box\">");
		Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
		Str.append("<div class=\"caption\" style=\"float: none\">Documents</div></div>");
		Str.append("<div class=\"portlet-body portlet-empty\">");
		Str.append("<div class=\"tab-pane\" id=\"\"></div>");
		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Document(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "salesorder-dash-docs.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount++;
			}
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql += " ORDER BY doc_id DESC"
					+ " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = StartRec - 1;

				Str.append("<a href=\"../portal/docs-update.jsp?add=yes&so_id=").append(so_id).append("\">Add New Document...</a><br><br>");
				Str.append("\n<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th width=\"5%\">#</th>\n");
				Str.append("<th>Document Details</th>\n");
				Str.append("<th width=\"20%\">Actions</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					so_desc = crs.getString("so_desc");
					Str.append("<tr>\n<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					if (!crs.getString("doc_value").equals("")) {
						File f = new File(SODocPath(comp_id) + crs.getString("doc_value"));
						Str.append("<td valign=\"top\" align=\"left\">");
						Str.append("<a href=../Fetchdocs.do?sodoc_id=").append(crs.getString("doc_id")).append(">");
						Str.append(crs.getString("doc_title"));
						Str.append(" (").append(ConvertFileSizeToBytes(FileSize(f))).append(")</a><br>");
						Str.append(crs.getString("doc_remarks")).append("</td>\n");
					} else {
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("doc_title"));
						Str.append(" (").append(crs.getString("doc_id")).append(") (0 Bytes)<br>");
						Str.append(crs.getString("doc_remarks")).append("</td>\n");
					}

					if (!so_id.equals("0")) {
						Str.append("<td valign=\"top\" align=\"left\">");
						Str.append("<a href=\"../portal/docs-update.jsp?update=yes&so_id=").append(so_id);
						Str.append("&doc_id=").append(crs.getString("doc_id")).append("\">Update Document</a></td>\n");
					}
					Str.append("</tr>\n");
				}
				crs.close();
				Str.append("</table>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			msg = "";
			Str.append("<br><br><br><br><center><font color=red><b>No Document(s) found!</b></font></center><br><br>");
		}
		Str.append("</div>\n");
		Str.append("</div>\n");
		Str.append("</div>\n");
		return Str.toString();
	}
}
