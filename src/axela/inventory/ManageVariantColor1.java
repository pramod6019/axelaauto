package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageVariantColor1 extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
			+ " &gt; <a href=../inventory/managevariantcolor.jsp?all=yes>Variant Color</a>:";
	public String LinkListPage = "../inventory/managevariantcolor.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=../inventory/managevariantcolor-update.jsp?Add=yes>Add Variant Color...</a>";
	public String ExportPerm = "";
	public String emp_id = "", branch_id = "";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String variantcolour_id = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			// {"ID", "numeric", "variantcolour_id"},
			{"Name", "text", "variantcolour_name"},
			{"Code", "text", "variantcolour_code"},
			{"Entry By", "text", "variantcolour_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "variantcolour_entry_date"},
			{"Modified By", "text", "variantcolour_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "variantcolour_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
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
				variantcolour_id = CNumeric(PadQuotes(request.getParameter("variantcolour_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND variantcolour_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Variant Color(s)!";
					StrSearch = StrSearch + " and variantcolour_id > 0";
				} else if (!(variantcolour_id.equals("0"))) {
					msg = msg + "<br>Results for Variant Color ID = " + variantcolour_id + "!";
					StrSearch = StrSearch + " and variantcolour_id = " + variantcolour_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND variantcolour_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("variantcolourstrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			// to know no of records depending on search

			StrSql = "SELECT variantcolour_id, variantcolour_name, variantcolour_code"
					+ " FROM " + compdb(comp_id) + "axela_inventory_variantcolour"
					+ " WHERE 1 = 1";

			CountSql = "SELECT COUNT(DISTINCT variantcolour_id)"
					+ " FROM " + compdb(comp_id) + "axela_inventory_variantcolour"
					+ " WHERE 1 = 1";

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Variant Color(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "../inventory/managevariantcolor.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " ORDER BY variantcolour_id DESC";
				StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					String active = "";
					StrHTML = "";
					int count = StartRec - 1;
					Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
					Str.append("<tr align=center>\n");
					Str.append("<th width=5%>#</th>\n");
					Str.append("<th width=35%>Variant Color </th>\n");
					Str.append("<th width=35%>Code</th>\n"); // 1
					Str.append("<th width=20%>Actions</th>\n");
					Str.append("</tr>\n");

					while (crs.next()) {
						count = count + 1;

						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("variantcolour_name")).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("variantcolour_code")).append("</td>\n");// 2
						Str.append("<td valign=top><a href=\"../inventory/managevariantcolor-update.jsp?Update=yes&variantcolour_id=").append(crs.getString("variantcolour_id"))
								.append(" \">Update Variant Color</a></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</table>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				Str.append("<br><br><br><br><b><font color=red>No Variant Color(s) Found!</font></b><br><br>");
			}
		}
		return Str.toString();
	}
}
