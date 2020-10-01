package axela.sales;

//Bhagwan Singh
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageLostCase1 extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managelostcase1.jsp?all=yes>List Lost Case 1</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managelostcase1-update.jsp?add=yes>Add Lost Case 1...</a>";
	public String ExportPerm = "";
	public String emp_id = "0", branch_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String lostcase1_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"ID", "numeric", "lostcase1_id"},
			{"Name", "text", "lostcase1_name"},
			{"Entry By", "text", "lostcase1_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "lostcase1_entry_date"},
			{"Modified By", "text", "lostcase1_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "lostcase1_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				lostcase1_id = CNumeric(PadQuotes(request.getParameter("lostcase1_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND lostcase1_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results For All Lost Case 1!";
					StrSearch = StrSearch + " AND lostcase1_id > 0";
				}

				if (!(lostcase1_id.equals("0"))) {
					msg = msg + "<br>Results For Lost Case 1 ID = " + lostcase1_id + "!";
					StrSearch = StrSearch + " AND lostcase1_id = " + lostcase1_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND lostcase1_id = 0";
					} else {
						msg = "Results For Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("lostcase1strsql", StrSearch, request);
				}
				StrHTML = Listdata();
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

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();

		// Check PageCurrent is valid for parse int
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			// to know no of records depending on search

			StrSql = "SELECT lostcase1_id, lostcase1_name, COALESCE(COUNT(DISTINCT(lostcase2_id)),0) AS lostcase2count";

			CountSql = "SELECT Count(DISTINCT lostcase1_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 ON lostcase2_lostcase1_id = lostcase1_id"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			// SOP("CountSql---------" + CountSql);
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			if (TotalRecords != 0) {
				StrSql = StrSql + " GROUP BY lostcase1_id";
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Lost Case 1";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "managelostcase1.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " ORDER BY lostcase1_id DESC";
				StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Lost Case 1 Details</th>\n");
					Str.append("<th data-hide=\"phone\" width = 20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top align=left >").append(crs.getString("lostcase1_name")).append(" </td>\n");
						Str.append("<td valign=top align=left ><a href=\"managelostcase1-update.jsp?update=yes&lostcase1_id=").append(crs.getString("lostcase1_id"))
								.append(" \"> Update Lost Case 1</a>");
						Str.append("<br><a href=\"managelostcase2.jsp?lostcase1_id=").append(crs.getString("lostcase1_id")).append(" \">List Lost Case 2</a>" + " (")
								.append(crs.getString("lostcase2count")).append(")");
						Str.append("</td>");
						Str.append("</tr>\n");
					}
					crs.close();
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Lost Case 1(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
