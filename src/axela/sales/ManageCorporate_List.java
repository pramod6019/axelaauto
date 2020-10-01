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

public class ManageCorporate_List extends Connect {

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../portal/manager.jsp\">Business Manager</a>"
			+ " &gt; <a href=\"managecorporate-list.jsp?all=yes\">List Corporates</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=\"managecorporate-update.jsp?add=yes\">Add New Corporate...</a>";
	public String ExportPerm = "";
	public String emp_id = "0", branch_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String active = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String corporate_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Corporate ID", "numeric", "corporate_id"},
			{"Corporate Name", "text", "corporate_name"},
			{"Active", "boolean", "corporate_active"},
			{"Entry By", "text", "corporate_entry_id IN (SELECT emp_id FROM axela_emp WHERE emp_name"},
			{"Entry Date", "date", "corporate_entry_date"},
			{"Modified By", "text", "corporate_modified_id IN (SELECT emp_id FROM axela_emp WHERE emp_name"},
			{"Modified Date", "date", "corporate_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = (session.getAttribute("emp_id")).toString();
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				corporate_id = CNumeric(PadQuotes(request.getParameter("corporate_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND corporate_id = 0";
				} else if ("yes".equals(all)) {
					msg += "<br>Results for All Corporates!";
					StrSearch += " AND corporate_id > 0";
				} else if (!corporate_id.equals("0")) {
					msg += "<br>Results for Corporate ID = " + corporate_id + "!";
					StrSearch += " AND corporate_id = " + corporate_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch += " AND corporate_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				StrHTML = ListData();
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListData() {
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

			StrSql = "SELECT corporate_id, corporate_name, "
					+ " corporate_code, corporate_cat,"
					+ " corporate_active,"
					+ " brand_name";

			CountSql = "SELECT COUNT(DISTINCT corporate_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry_corporate"
					+ " INNER JOIN axela_brand ON brand_id = corporate_brand_id"
					+ " WHERE 1 = 1";

			StrSql += SqlJoin + StrSearch;
			CountSql += SqlJoin + StrSearch;

			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			// SOP("ListData======" + StrSqlBreaker(StrSql));
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Corporate(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "managecorporate-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount++;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql += " ORDER BY corporate_id DESC"
						+ " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=\"5%\">#</th>\n");
					Str.append("<th>Corporate Details</th>\n");
					Str.append("<th>Brand</th>\n");
					Str.append("<th data-hide=\"phone\">Code</th>\n");
					Str.append("<th data-hide=\"phone\">Category</th>\n");
					Str.append("<th data-hide=\"phone, tablet\" width=\"20%\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count++;
						if (crs.getString("corporate_active").equals("0")) {
							active = "<font color=red > [Inactive] </font>";
						} else {
							active = "";
						}
						Str.append("<tr>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("corporate_name")).append(active).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("brand_name")).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("corporate_code")).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("corporate_cat")).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">");
						Str.append("<a href=\"managecorporate-update.jsp?update=yes&corporate_id=").append(crs.getString("corporate_id")).append("\">Update Corporate</a></td>\n");
					}
					crs.close();
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				} catch (Exception ex) {
					SOPError(ClientName + "===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Corporate(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
