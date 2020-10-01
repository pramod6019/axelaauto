package axela.sales;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Salesorder_Doc_List extends Connect {

	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String so_id = "0";
	public String StrHTML = "";
	public String CountSql = "";
	public String Sqljoin = "";
	public String StrJoin = "";
	public String StrSql = "";
	public String PageCurrents = "0";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCurrent = 0;
	public int PageCount = 10;
	public String QueryString = "";
	public String fileext = "";
	public String fileName = "";
	public String StrSearch = "";
	public String LinkHeader = "";
	public String LinkAddPage = "";
	public String BranchAccess = "";
	public String ExeAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));

				if (!so_id.equals("0")) {
					LinkHeader = "<a href=\"../poratl/home.jsp\">Home</a>"
							+ " &gt; <a href=\"veh-salesorder.jsp\">Sales Orders</a>"
							+ " &gt; <a href=\"veh-salesorder-list.jsp?so_id=" + so_id + "\">Sales Order Id: " + so_id + "</a>"
							+ " &gt; <a href=\"veh-salesorder-doc-list.jsp?so_id=" + so_id + "\">List Documents</a>:";
					LinkAddPage = "<a href=\"../portal/docs-update.jsp?add=yes&so_id=" + so_id + "\">Add New Document...</a>";
					StrSearch = " AND doc_so_id = " + so_id + "";
				}
				StrHTML = ListDocs();
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

	public String ListDocs() {
		StringBuilder Str = new StringBuilder();
		int TotalRecords;
		int StartRec;
		int EndRec;
		String PageURL;
		int PageListSize = 10;
		String effective = "";
		String duedate;

		if ((PageCurrents.equals("0"))) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		StrSql = "SELECT doc_id, doc_value, doc_title, doc_remarks, doc_effective,"
				+ " doc_duedays, doc_wf_title, so_delivered_date, so_date";

		StrJoin = " FROM " + compdb(comp_id) + "axela_sales_so_docs"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so on so_id = doc_so_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = so_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id"
				+ " WHERE 1 = 1" + BranchAccess + ExeAccess;

		CountSql = " SELECT COUNT(DISTINCT doc_id)";

		StrSql += StrJoin + StrSearch;
		CountSql += StrJoin + StrSearch;

		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

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
			PageURL = "veh-salesorder-doc-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount++;
			}
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

			StrSql += " GROUP BY doc_id"
					+ " ORDER BY doc_id DESC"
					+ " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = 0;
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th width=\"5%\">#</th>");
				Str.append("<th>Documents</th>");
				Str.append("<th>Title</th>\n");
				Str.append("<th>Effective From</th>\n");
				Str.append("<th>Due Date</th>\n");
				Str.append("<th width=\"20%\">Actions</th>");
				Str.append("</tr>\n");
				while (crs.next()) {
					if (crs.getString("doc_effective").equals("0")) {
						effective = "";
					} else if (crs.getString("doc_effective").equals("1")) {
						effective = "Sales Order Date";
					} else if (crs.getString("doc_effective").equals("2")) {
						effective = "Delivery Date";
					}

					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">");
					if (!crs.getString("doc_value").equals("")) {
						if (!new File(SODocPath(comp_id)).exists()) {
							new File(SODocPath(comp_id)).mkdirs();
						}

						File f = new File(SODocPath(comp_id) + crs.getString("doc_value"));
						Str.append("<a href=\"../Fetchdocs.do?").append(QueryString).append("&sodoc_id=").append(crs.getString("doc_id")).append("\">");
						Str.append(crs.getString("doc_title")).append(" (").append(ConvertFileSizeToBytes(FileSize(f))).append(")</a>");
						if (!crs.getString("doc_remarks").equals("")) {
							Str.append("<br>").append(crs.getString("doc_remarks"));
						}
					} else {
						Str.append("&nbsp;");
					}
					Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(crs.getString("doc_wf_title")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(effective).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">");
					if (crs.getString("doc_effective").equals("0")) {
						Str.append("-");
					} else {
						if (crs.getString("doc_effective").equals("1")) {
							duedate = ToShortDate(AddHoursDate(StringToDate(crs.getString("so_date")), crs.getDouble("doc_duedays"), 0, 0));
							Str.append(strToShortDate(duedate));
						}

						if (crs.getString("doc_effective").equals("2") && !crs.getString("so_delivered_date").equals("")) {
							duedate = ToShortDate(AddHoursDate(StringToDate(crs.getString("so_delivered_date")), crs.getDouble("doc_duedays"), 0, 0));
							Str.append(strToShortDate(duedate));
						}

						if (crs.getString("doc_effective").equals("2") && crs.getString("so_delivered_date").equals("")) {
							Str.append("-");
						}
					}
					Str.append("</td>\n<td valign=\"top\" align=\"left\">");
					if (so_id.equals("0")) {
						Str.append("<a href=\"../portal/docs-update.jsp?update=yes&doc_id=").append(crs.getString("doc_id")).append("\">Update Document</a>");
					} else {
						Str.append("<a href=\"../portal/docs-update.jsp?update=yes&so_id=").append(so_id).append("&doc_id=").append(crs.getString("doc_id")).append("\">Update Document</a>");
					}
					Str.append("&nbsp;</td>\n");
				}
				Str.append("</tr>\n</table>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		} else {
			msg = "";
			Str.append("<br><br><br><br><font color=red><b>No Document(s) found!</b></font><br><br>");
		}
		return Str.toString();
	}
}
