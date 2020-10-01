package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Coupon_List extends Connect {
	// ///// List page links
	public String camp_name = "";
	public String LinkHeader = "";

	public String LinkListPage = "coupon-list.jsp?all=yes";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=couponcampaign-update.jsp?add=yes style=text-align:right></a>";
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
			{"Keyword", "text", "keyword_arr"},
			// {"Coupon ID", "numeric", "	coupon_id"},
			{"Campaign Name", "text", "couponcampaign_id IN (SELECT couponcampaign_id FROM compdb.axela_service_coupon_campaign WHERE couponcampaign_name"},
			{"Contact", "text", "customer_name"},
			{"Issued Executive", "text", "couponcampaign_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Issue Time", "date", "coupon_issue_time"},
			{"Redeem Executive", "text", "couponcampaign_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Redeem Time", "date", "coupon_redeem_time"}
			// {"Department Name", "text", "department_name"},
			// {"Entry By", "text", "couponcampaign_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			// {"Entry Date", "date", "couponcampaign_entry_date"},
			// {"Modified By", "text", "couponcampaign_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			// {"Modified Date", "date", "couponcampaign_modified_date"}
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
				CheckPerm(comp_id, "emp_role_id", request, response);
				// branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				couponcampaign_id = CNumeric(PadQuotes(request.getParameter("couponcampaign_id")));
				camp_name = request.getParameter("couponcampaign_name");
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				camp_name = ExecuteQuery("SELECT couponcampaign_name from " + compdb(comp_id) + "axela_service_coupon_campaign where couponcampaign_id= " + couponcampaign_id);
				LinkHeader = "<li><a href=../portal/home.jsp>Home</a> &gt;&nbsp; </li>"
						+ "<li><a href=../service/index.jsp>Service</a> &gt;&nbsp;  </li>"
						+ "<li><a href=couponcampaign-list.jsp?all=yes>List Campaign</a> &gt;&nbsp; </li>"
						+ "<li><a href=coupon-list.jsp?couponcampaign_id=" + couponcampaign_id
						+ ">List Coupon</a><b>:</b></li>";
				if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Coupon Campaigns!";
					StrSearch = StrSearch + " AND couponcampaign_id > 0";
				} else if (!couponcampaign_id.equals("0")) {
					msg = msg + "<br>Results for Coupon Campaign = " + couponcampaign_id + "!";
					StrSearch = StrSearch
							+ " AND coupon_couponcampaign_id = " + couponcampaign_id + "";
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
		// CONCAT(title_desc, contact_fname,contact_lname) as customer,
		// couponcampaign_id,
		// coupon_contact_id,
		// coupon_issue_time,
		// coupon_issue_emp_id,
		// coupon_redeem_time,
		// coupon_redeem_veh_id,
		// coupon_redeem_jc_id,
		// coupon_redeem_invoice_id,
		// coupon_redeem_emp_id,
		// couponcampaign_couponvalue,
		// couponcampaign_budget
		// FROM
		// " + compdb(comp_id) + "axela_service_coupon_campaign
		// INNER JOIN " + compdb(comp_id) + "axela_service_coupon ON coupon_couponcampaign_id = couponcampaign_id
		// INNER JOIN axela_customer_contact on contact_id = coupon_contact_id
		// INNER JOIN axela_title on title_id = contact_title_id
		// where couponcampaign_id = 4
		// to know no of records depending on search
		StrSql = "SELECT coupon_id, COALESCE (CONCAT(title_desc,contact_fname,contact_lname), '') AS customer_name, couponcampaign_id,"
				+ " coupon_contact_id, couponcampaign_name, coupon_issue_time,"
				+ "	COALESCE (couponissued.emp_id, 0) AS couponissued_emp_id,"
				+ " COALESCE (couponissued.emp_name, '') AS couponissued_emp_name,"
				+ " COALESCE (couponredeemed.emp_id, 0) AS couponredeemed_emp_id,"
				+ " COALESCE (couponredeemed.emp_name, '') AS couponredeemed_emp_name,"
				+ " coupon_redeem_time, coupon_redeem_veh_id,"
				+ " coupon_redeem_jc_id, coupon_redeem_invoice_id, coupon_redeem_emp_id,"
				+ " couponcampaign_couponvalue, couponcampaign_budget";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_service_coupon"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_coupon_campaign ON couponcampaign_id =  coupon_couponcampaign_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = coupon_contact_id"
				+ "	LEFT JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp couponissued ON couponissued.emp_id = coupon_issue_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp couponredeemed ON couponredeemed.emp_id = coupon_redeem_emp_id"
				+ " WHERE 1 = 1 ";

		CountSql = "SELECT COUNT(DISTINCT coupon_id )";

		StrSql = StrSql + SqlJoin;
		CountSql = CountSql + SqlJoin;

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		// SOP("CountSql--couponlist--" + CountSql);
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

			// if limit ie. 10 > totalrecord
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}

			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
					+ " of " + TotalRecords + " Coupon(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "coupon-list.jsp?" + strq + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

			StrSql = StrSql + " ORDER BY coupon_issue_time DESC";

			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			// SOP("StrSql-----" + StrSql);
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");

				Str.append("<th>Campaign Name</th>\n");
				Str.append("<th>Contact</th>\n");
				Str.append("<th>Issued Executive</th>\n");
				Str.append("<th>Issue Time</th>\n");
				Str.append("<th>Redeem Executive</th>\n");
				Str.append("<th>Redeem Time</th>\n");
				// Str.append("<th>coupon_redeem_veh_id</th>\n");
				// Str.append("<th>coupon_redeem_jc_id</th>\n");
				// Str.append("<th>coupon_redeem_invoice_id</th>\n");
				Str.append("<th>Coupon Value</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");

					Str.append("<td><a href=\"couponcampaign-list.jsp?all=yes&couponcampaign_id=").append(crs.getString("couponcampaign_id")).append("\">");
					Str.append(crs.getString("couponcampaign_name")).append("</a>").append("</td>\n");
					Str.append("<td><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("coupon_contact_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a></td>\n");
					Str.append("<td><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("couponissued_emp_id")).append("\">");
					Str.append(crs.getString("couponissued_emp_name")).append("</a></td>\n");
					Str.append("<td align=center>").append(strToShortDate(crs.getString("coupon_issue_time"))).append("</td>\n");
					Str.append("<td><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("couponredeemed_emp_id")).append("\">");
					Str.append(crs.getString("couponredeemed_emp_name")).append("</a></td>\n");
					Str.append("<td align=center>").append(strToShortDate(crs.getString("coupon_redeem_time"))).append("</td>\n");
					// Str.append("<td>").append(crs.getString("coupon_redeem_veh_id")).append("</td>\n");
					// Str.append("<td>").append(crs.getString("coupon_redeem_jc_id")).append("</td>\n");
					// Str.append("<td>").append(crs.getString("coupon_redeem_invoice_id")).append("</td>\n");
					Str.append("<td align=right>").append(IndFormat(crs.getString("couponcampaign_couponvalue"))).append("</td>\n");
					// Str.append("<td><a href=\"couponcampaign-update.jsp?update=yes&couponcampaign_id=").append(crs.getString("couponcampaign_id")).append("\">Update Campaign</a></br>");
					// Str.append("<a href=\"couponcampaign-list.jsp?all=yes&couponcampaign_id=").append(crs.getString("couponcampaign_id")).append("\">List Coupons");
					// Str.append(" (").append(crs.getString("couponcampaign_couponcount")).append(")</a>");
					// Str.append("</td>\n");
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
