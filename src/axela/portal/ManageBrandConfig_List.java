package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageBrandConfig_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a> &gt;&nbsp; </li><li><a href=manager.jsp>Business Manager</a> &gt;&nbsp;  </li><li><a href=managebrandconfig-list.jsp>List Brand Config</a><b>:</b></li>";
	public String LinkListPage = "managebrandconfig-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=managebrandconfig-update.jsp?add=yes style=text-align:right>Add Brand Config...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String brand_id = "0";
	public String StrHTML = "";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String SqlJoin = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String strq = "";
	public String brandconfig_id = "0";
	public String all = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"BrandConfig ID", "numeric", "brandconfig_id"},
			{"Name", "text", "brand_name"},
			{"Entry By", "text", "brandconfig_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "brandconfig_entry_date"},
			{"Modified By", "text", "brandconfig_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "brandconfig_modified_date"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				recperpage = Integer.parseInt(GetSession("emp_recperpage",
						request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				brandconfig_id = CNumeric(PadQuotes(request
						.getParameter("brandconfig_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND brandconfig_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Brand Configs!";
					StrSearch = StrSearch + " and brandconfig_id > 0";
				} else if (!(brandconfig_id.equals("0"))) {
					msg = msg + "<br>Results for brandconfig_id = " + brandconfig_id
							+ "!";
					StrSearch = StrSearch + " and brandconfig_id = "
							+ brandconfig_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}
				/*
				 * if (!StrSearch.equals("")) { SetSession("brandstrsql", StrSearch, request); }
				 */
				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search
		StrSql = "SELECT brandconfig_id, brand_id, brandconfig_brand_id,"
				+ " COALESCE(brand_name,'')AS brand_name, "
				+ " brandconfig_deallocatestock_days, brandconfig_deallocatestock_amountperc";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_brand_config"
				+ " INNER JOIN axela_brand ON brand_id = brandconfig_brand_id"
				+ " WHERE 1 = 1";

		// SOP("StrSql===" + StrSqlBreaker(StrSql));

		CountSql = "SELECT COUNT(DISTINCT brandconfig_id )";

		StrSql = StrSql + SqlJoin;
		CountSql = CountSql + SqlJoin;

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

			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
					+ " of " + TotalRecords + " Brand Config(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "managebrandconfig-list.jsp?" + strq + QueryString
					+ "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
					PageListSize);

			StrSql = StrSql + " order by brandconfig_id desc";

			// StrSql = StrSql + " order by brandconfig_id desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage
					+ "";
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Brand Name</th>\n");
				// Str.append("<th>Days</th>\n");
				// Str.append("<th data-hide=\"phone\">Percentage</th>\n");
				Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td>").append(crs.getString("brand_name")).append("</td>\n");
					// Str.append("<td>").append(crs.getString("brandconfig_deallocatestock_days")).append("</td>\n");
					// Str.append("<td>").append(crs.getString("brandconfig_deallocatestock_amountperc")).append("</td>\n");
					Str.append(
							"<td align=left> "
									+ " <a href=\"managebrandconfig-update.jsp?update=yes&brand_id=")
							.append(crs.getString("brand_id")).append("&brandconfig_id=")
							.append(crs.getString("brandconfig_id"))
							.append(" \">Update Brand Config</a>\n");
					// Str.append("<br><a href=\"../portal/so-waitingperioddays-list.jsp?dr_brand=" + crs.getString("brandconfig_brand_id") +
					// "&brandconfig_id=");
					// Str.append(crs.getString("brandconfig_id"));
					// Str.append(" \">List SO Waiting Period Days</a></td>\n");

					Str.append("<br><a href=\"../portal/canned-email-list.jsp?dr_brand=" + crs.getString("brandconfig_brand_id") +
							"&brandconfig_id=");
					Str.append(crs.getString("brandconfig_id"));
					Str.append(" \">List Canned Email</a>\n");

					Str.append("<br><a href=\"../portal/canned-sms-list.jsp?dr_brand=" + crs.getString("brandconfig_brand_id") +
							"&brandconfig_id=");
					Str.append(crs.getString("brandconfig_id"));
					Str.append(" \">List Canned SMS</a></td>\n");

				}
				crs.close();
				Str.append("</tr>\n");
				Str.append("</tbody></table></div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No Brand Config (s) Found!</font><br><br>";
		}
		return Str.toString();
	}
}
