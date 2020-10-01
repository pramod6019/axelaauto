package axela.customer;
//@Bhagwan Singh 11 feb 2013

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Customer_Docs_List extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String PageCurrents = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCurrent = 0;
	public int PageCount = 10;
	public String QueryString = "";
	public String msg = "";
	public String doc_id = "0";
	public String doc_customer_id = "0";
	public String all = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String CountSql = "";
	public String Sqljoin = "";
	public String StrSearch = "";
	public String customer_id = "0";
	public String customer_name = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));
				all = PadQuotes(request.getParameter("all"));
				customer_name = ExecuteQuery("SELECT customer_name FROM " + compdb(comp_id) + "axela_customer"
						+ " WHERE customer_id = " + customer_id);
				if (!customer_name.equals("")) {
					if ("yes".equals(all)) {
						msg = "Results for all Documents";
						StrSearch += " AND doc_id > 0";
					} else if (!doc_id.equals("0")) {
						StrSearch += " AND doc_id = " + doc_id + "";
					}
					StrHTML = ListData();
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Customer!");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListData() {
		StringBuilder Str = new StringBuilder();
		int TotalRecords;
		int StartRec;
		int EndRec;
		String PageURL;
		int PageListSize = 10;

		if ((PageCurrents.equals("0"))) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		// to know no of records depending on search
		StrSql = "SELECT doc_id, doc_value, doc_title, doc_remarks";

		CountSql = "SELECT COUNT(DISTINCT doc_id)";

		Sqljoin = " FROM " + compdb(comp_id) + "axela_customer_docs"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON doc_customer_id = customer_id"
				+ " WHERE doc_customer_id = " + customer_id + "";

		StrSql += Sqljoin;
		CountSql += Sqljoin;

		TotalRecords = Integer.parseInt(CNumeric(ExecuteQuery(CountSql)));
		if (!StrSearch.equals("")) {
			StrSql += StrSearch;
			CountSql += StrSearch;
		}

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
			PageURL = "customer-docs-list.jsp?" + QueryString + "&PageCurrent=";
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
				Str.append("<div class=\"  table\">\n");
				Str.append("<table class=\"table table-bordered  table-hover\" class=\"listtable\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=\"center\">\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Document Details</th>\n");
				Str.append("<th width=\"20%\">Actions</th>\n");
				Str.append("</tr></thead>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"center\"><b>").append(count).append(".</b></td>\n");
					if (!crs.getString("doc_value").equals("")) {
						if (!new File(CustomerDocPath(comp_id)).exists()) {
							new File(CustomerDocPath(comp_id)).mkdirs();
						}
						File f = new File(CustomerDocPath(comp_id) + crs.getString("doc_value"));
						Str.append("<td height=\"20\" valign=\"top\" align=\"left\">");
						Str.append("<a href=\"../Fetchdocs.do?").append(QueryString).append("&customer_doc_id=").append(crs.getString("doc_id")).append("\">");
						Str.append(crs.getString("doc_title")).append(" (").append(ConvertFileSizeToBytes(FileSize(f))).append(")</a><br>");
						Str.append(crs.getString("doc_remarks")).append("</td>\n");
					} else {
						Str.append("<td height=\"20\" valign=\"top\" align=\"left\">&nbsp;</td>\n");
					}
					if (!customer_id.equals("0")) {
						Str.append("<td height=20 valign=top align=left >");
						Str.append("<a href=\"../portal/docs-update.jsp?update=yes&customer_id=").append(customer_id).append("&doc_id=").append(crs.getString("doc_id"))
								.append("\">Update Document</a></td>\n");
					}
					Str.append("</tr>\n");
				}
				crs.close();
				Str.append("</table></div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			msg = "";
			Str.append("<br><br><br><br><font color=\"red\"><b>No Document(s) found!</b></font><br><br>");
		}
		return Str.toString();
	}
}
