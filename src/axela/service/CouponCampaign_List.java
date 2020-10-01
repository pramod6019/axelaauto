package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class CouponCampaign_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a> &gt;&nbsp; </li><li><a href=../service/index.jsp>Service</a> &gt;&nbsp;  </li><li><a href=couponcampaign-list.jsp?all=yes>List Coupon Campaign</a><b>:</b></li>";
	public String LinkListPage = "couponcampaign-list.jsp?all=yes";
	public String LinkExportPage = "coupon-export.jsp";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=couponcampaign-update.jsp?add=yes style=text-align:right>Add Coupon Campaign...</a>";
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
	public String couponcampaign_id = "0";
	public String all = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			// {"Keyword", "text", "keyword_arr"},
			{"Coupon Campaign ID", "numeric", "couponcampaign_id"},
			{"Coupon Campaign Name", "text", "couponcampaign_name"},
			{"Brand", "text", "brand_name"},
			{"Department Name", "text", "department_name"},
			{"Campaign Type", "text", "couponcampaign_campaigntype_name"},
			{"Campaign Start Date", "date", "couponcampaign_startdate"},
			{"Campaign End Date", "date", "couponcampaign_enddate"},
			{"Coupon Count", "numeric", "couponcampaign_couponcount"},
			{"Coupon Value", "numeric", "couponcampaign_couponvalue"},
			{"Entry By", "text", "couponcampaign_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "couponcampaign_entry_date"},
			{"Modified By", "text", "couponcampaign_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "couponcampaign_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				// brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_service_coupon_campaign_access", request, response);
				// branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				couponcampaign_id = CNumeric(PadQuotes(request.getParameter("couponcampaign_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);

				if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Coupon Campaigns!";
					StrSearch = StrSearch + " AND couponcampaign_id> 0";
				} else if (!(couponcampaign_id.equals("0"))) {
					msg = msg + "<br>Results for Coupon Campaign = " + couponcampaign_id + "!";
					StrSearch = StrSearch
							+ " AND couponcampaign_id = " + couponcampaign_id + "";
				} else if (advSearch.equals("Search")) {
					// for keyword search
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}
				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

		// SELECT
		// COUNT(couponcampaign_id)
		// FROM
		// " + compdb(comp_id) + "axela_service_coupon
		// INNER JOIN " + compdb(comp_id) + "axela_service_coupon_campaign ON couponcampaign_id = coupon_couponcampaign_id
		// LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = coupon_contact_id
		// LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id
		// LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = coupon_issue_emp_id
		// WHERE
		// couponcampaign_id = 14
		// to know no of records depending on search
		StrSql = "SELECT brand_name, couponcampaign_id, couponcampaign_name,"
				+ " couponcampaintype_name, department_name, couponcampaign_startdate,"
				+ " couponcampaign_enddate, couponcampaign_couponcount,"
				+ " couponcampaign_couponvalue, couponcampaign_budget";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_service_coupon_campaign"
				+ " INNER JOIN " + "axela_brand ON brand_id = couponcampaign_brand_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_couponcampain_type ON couponcampaintype_id = couponcampaign_campaigntype_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp_department ON department_id = couponcampaign_department_id"
				+ " WHERE 1 = 1";

		// SOP("StrSql===" + StrSqlBreaker(StrSql));

		CountSql = "SELECT COUNT(DISTINCT couponcampaign_id )";

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
					+ " of " + TotalRecords + " Coupon Campaigns(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "couponcampaign-list.jsp?" + strq + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
					PageListSize);

			StrSql = StrSql + " ORDER BY couponcampaign_id DESC";

			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

			// SOP("StrSql-----" + StrSql);
			SOP("CouponCampaignlist------" + StrSql);
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Coupon Campaign</th>\n");
				Str.append("<th>Brand</th>\n");
				Str.append("<th>Department</th>\n");
				Str.append("<th>Coupon Campaign Type</th>\n");
				Str.append("<th>Start Date</th>\n");
				Str.append("<th>End Date</th>\n");
				Str.append("<th>Coupon Count</th>\n");
				Str.append("<th>Coupon Value</th>\n");
				Str.append("<th>Campaign Budget</th>\n");
				Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td>").append(crs.getString("couponcampaign_name")).append("</td>\n");
					Str.append("<td>").append(crs.getString("brand_name")).append("</td>\n");
					Str.append("<td>").append(crs.getString("department_name")).append("</td>\n");
					Str.append("<td>").append(crs.getString("couponcampaintype_name")).append("</td>\n");
					Str.append("<td align=center>").append(strToShortDate(crs.getString("couponcampaign_startdate"))).append("</td>\n");
					Str.append("<td align=center>").append(strToShortDate(crs.getString("couponcampaign_enddate"))).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("couponcampaign_couponcount")).append("</td>\n");
					Str.append("<td align=right>").append(IndFormat(crs.getString("couponcampaign_couponvalue"))).append("</td>\n");
					Str.append("<td align=right>").append(IndFormat(crs.getString("couponcampaign_budget"))).append("</td>\n");
					Str.append("<td><a href=\"couponcampaign-update.jsp?update=yes&couponcampaign_id=").append(crs.getString("couponcampaign_id")).append("\">Update Campaign</a></br>");
					Str.append("<a href=\"coupon-list.jsp?couponcampaign_id=").append(crs.getString("couponcampaign_id")).append("\">List Coupons");
					Str.append(" (").append(crs.getString("couponcampaign_couponcount")).append(")</a>");
					Str.append("</td>\n");
				}
				crs.close();
				Str.append("</tr>\n");
				Str.append("</tbody></table></div>\n");
			} catch (Exception ex) {
				SOPError(this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No Coupon Campaign(s) Found!</font><br><br>";
		}
		return Str.toString();
	}
}
