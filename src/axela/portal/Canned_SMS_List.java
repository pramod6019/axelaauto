package axela.portal;
//Murali 10th aug

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Canned_SMS_List extends Connect {
	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"managebrandconfig-list.jsp\">List Brand Config</a>"
			+ " &gt; <a href=\"canned-sms-list.jsp?all=yes\">List Canned SMS</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=\"canned-sms-update.jsp?add=yes&brand_id=\" + dr_brand + \">Add New Canned SMS ...</a>";
	public String LinkPrintPage = "";
	public String ExportPerm = "";
	public String advhtml = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String StrHTML = "";
	public String msg = "";
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
	public String ServiceTax = "";
	public String all = "";
	public String Up = "";
	public String Down = "";
	public String smart = "";
	public String dr_brand = "0";
	public String cannedsms_id = "0";
	public String course_name = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Canned SMS ID", "numeric", "cannedsms_id"},
			{"Brand", "text", "brand_name IN (SELECT brand_name FROM axela_brand WHERE brand_name"},
			{"Type", "text", "branchtype_name IN (SELECT branchtype_name FROM axela_branch_type WHERE branchtype_name"},
			{"Name", "text", "cannedsms_name"},
			{"Entry By", "text", "cannedsms_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "cannedsms_entry_date"},
			{"Modified By", "text", "cannedsms_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "cannedsms_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_sms_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				msg = PadQuotes(request.getParameter("msg"));
				dr_brand = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				cannedsms_id = CNumeric(PadQuotes(request.getParameter("cannedsms_id")));
				all = PadQuotes(request.getParameter("all"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND cannedsms_id = 0";
				}
				else if (!(dr_brand.equals("0"))) {
					msg = msg + "<br>Results for Brand Code =" + dr_brand + "";
					StrSearch = StrSearch + " and cannedsms_brand_id =" + dr_brand + "";
				} else if (!(cannedsms_id.equals("0"))) {
					msg = msg + "<br>Results for Canned SMS Id=" + cannedsms_id + "";
					StrSearch = StrSearch + " AND cannedsms_id =" + cannedsms_id + "";
				}
				if (Up.equals("yes")) {
					MoveUp();
					response.sendRedirect(response.encodeRedirectURL("canned-sms-list.jsp?dr_brand=" + dr_brand + "&msg=SMS Moved UP Successfully!"));
				}
				if (Down.equals("yes")) {
					MoveDown();
					response.sendRedirect(response.encodeRedirectURL("canned-sms-list.jsp?dr_brand=" + dr_brand + "&msg=SMS Moved Down Successfully!"));
				}
				else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch += SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Results for all Canned Emails!";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
				}
				StrHTML = Listdata();
				// SOP("StrHTML====" + StrHTML);
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

		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		String StrJoin = "";

		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search
		StrSql = " SELECT cannedsms_id, cannedsms_name, brand_name, branchtype_name, cannedsms_brand_id,"
				+ " cannedsms_active ";

		StrJoin = " FROM " + compdb(comp_id) + "axela_canned_sms"
				+ " INNER JOIN axela_brand ON brand_id = cannedsms_brand_id"
				+ " INNER JOIN axela_branch_type ON branchtype_id = cannedsms_branchtype_id"
				+ " WHERE 1=1 ";
		CountSql = " SELECT COUNT(DISTINCT cannedsms_id) ";
		// if (!branch_id.equals("0")) {
		// StrSearch = StrSearch + " and branch_id = " + branch_id + " ";
		// }
		StrSql = StrSql + StrJoin;
		CountSql = CountSql + StrJoin;
		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		SOP("CountSql====" + CountSql);
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Canned SMS(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "canned-sms-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			if (all.equals("yes")) {
				StrSql = StrSql + "GROUP BY  cannedsms_id  ORDER BY brand_id desc";
			} else {
				StrSql = StrSql + "  ORDER BY brand_id ,cannedsms_branchtype_id,cannedsms_rank";
			}
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
			try {
				SOP("StrSql==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = 0;
				// Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Brand</th>\n");
				Str.append("<th data-toggle=\"true\">Type</th>\n");
				Str.append("<th data-hide=\"phone\">Name</th>\n");
				Str.append("<th data-hide=\"phone\">Order</th>\n");
				Str.append("<th data-hide=\"phone\">Action</th>\n");
				Str.append("</tr></thead><tbody>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td align=left>");
					if (!crs.getString("brand_name").equals("")) {
						Str.append(crs.getString("brand_name"));
					}
					if (!crs.getString("cannedsms_active").equals("1")) {
						Str.append("<font color='red'>&nbsp;[Inactive]</font>");
					}
					Str.append("</td>");
					Str.append("<td align=left>");
					if (!crs.getString("branchtype_name").equals("")) {
						Str.append(crs.getString("branchtype_name"));
					}
					Str.append("</td>");
					Str.append("<td align=left>");
					if (!crs.getString("cannedsms_name").equals("")) {
						Str.append(crs.getString("cannedsms_name"));
					}
					Str.append("</td>");
					Str.append("</td>");
					Str.append(
							"<td align=center><a href=\"canned-sms-list.jsp?Up=yes&cannedsms_id=")
							.append(crs.getString("cannedsms_id")).append("&dr_brand=").append(crs.getString("cannedsms_brand_id"))
							.append(" \">Up</a> - <a href=\"canned-sms-list.jsp?Down=yes&cannedsms_id=")
							.append(crs.getString("cannedsms_id")).append("&dr_brand=").append(crs.getString("cannedsms_brand_id"))
							.append(" \">Down</a></td>\n");
					Str.append("<td align=left><a href=\"../portal/canned-sms-update.jsp?update=yes&cannedsms_id=" + crs.getString("cannedsms_id"));
					Str.append(" \">Update SMS</a></td>\n");

				}
				Str.append("</tbody></table>\n");
				// Str.append("</div>");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No SMS(s) found!</font><br><br>";
		}
		return Str.toString();
	}

	public void MoveUp() {
		int tempRank;
		int cannedsms_rank;
		int cannedsms_brand_id;
		int cannedsms_branchtype_id;
		try {
			cannedsms_rank = Integer.parseInt(ExecuteQuery("SELECT cannedsms_rank "
					+ " FROM " + compdb(comp_id) + "axela_canned_sms"
					+ " WHERE cannedsms_id=" + cannedsms_id + ""));

			cannedsms_brand_id = Integer.parseInt(ExecuteQuery("SELECT cannedsms_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_canned_sms"
					+ " WHERE cannedsms_id = " + cannedsms_id));

			cannedsms_branchtype_id = Integer.parseInt(ExecuteQuery("SELECT cannedsms_branchtype_id "
					+ " FROM " + compdb(comp_id) + "axela_canned_sms"
					+ " WHERE cannedsms_id = " + cannedsms_id));

			tempRank = Integer.parseInt(ExecuteQuery("select MIN(cannedsms_rank) as min1"
					+ " FROM " + compdb(comp_id) + "axela_canned_sms"
					+ " WHERE cannedsms_brand_id = " + cannedsms_brand_id
					+ " AND cannedsms_branchtype_id = " + cannedsms_branchtype_id));

			if (cannedsms_rank != tempRank) {
				tempRank = cannedsms_rank - 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_canned_sms"
						+ " SET cannedsms_rank = " + cannedsms_rank
						+ " WHERE cannedsms_rank = " + tempRank
						+ " AND cannedsms_brand_id=" + cannedsms_brand_id
						+ " AND cannedsms_branchtype_id=" + cannedsms_branchtype_id;

				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_canned_sms "
						+ " SET cannedsms_rank=" + tempRank
						+ " WHERE cannedsms_rank = " + cannedsms_rank
						+ " AND cannedsms_id = " + cannedsms_id
						+ " AND cannedsms_brand_id = " + cannedsms_brand_id
						+ " AND cannedsms_branchtype_id=" + cannedsms_branchtype_id;
				updateQuery(StrSql);
			}
			dr_brand = Integer.toString(cannedsms_brand_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void MoveDown() {
		int tempRank;
		int cannedsms_rank;
		int cannedsms_brand_id;
		int cannedsms_branchtype_id;
		// SOP("sop==");
		try {
			cannedsms_rank = Integer.parseInt(ExecuteQuery("SELECT cannedsms_rank "
					+ " FROM " + compdb(comp_id) + "axela_canned_sms"
					+ " WHERE cannedsms_id=" + cannedsms_id + ""));

			cannedsms_brand_id = Integer.parseInt(ExecuteQuery("SELECT cannedsms_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_canned_sms"
					+ " WHERE cannedsms_id = " + cannedsms_id));

			cannedsms_branchtype_id = Integer.parseInt(ExecuteQuery("SELECT cannedsms_branchtype_id "
					+ " FROM " + compdb(comp_id) + "axela_canned_sms"
					+ " WHERE cannedsms_id = " + cannedsms_id));

			tempRank = Integer.parseInt(ExecuteQuery("select MAX(cannedsms_rank) as max1"
					+ " FROM " + compdb(comp_id) + "axela_canned_sms"
					+ " WHERE cannedsms_brand_id = " + cannedsms_brand_id
					+ " AND cannedsms_branchtype_id = " + cannedsms_branchtype_id));

			if (cannedsms_rank != tempRank) {
				tempRank = cannedsms_rank + 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_canned_sms"
						+ " SET cannedsms_rank = " + cannedsms_rank
						+ " WHERE cannedsms_rank = " + tempRank
						+ " AND cannedsms_brand_id=" + cannedsms_brand_id
						+ " AND cannedsms_branchtype_id=" + cannedsms_branchtype_id;

				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_canned_sms "
						+ " SET cannedsms_rank=" + tempRank
						+ " WHERE cannedsms_rank = " + cannedsms_rank
						+ " AND cannedsms_id = " + cannedsms_id
						+ " AND cannedsms_brand_id = " + cannedsms_brand_id
						+ " AND cannedsms_branchtype_id=" + cannedsms_branchtype_id;
				updateQuery(StrSql);
			}
			dr_brand = Integer.toString(cannedsms_brand_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
