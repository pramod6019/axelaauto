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

public class Canned_Email_List extends Connect {
	// ///// List page links
	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"managebrandconfig-list.jsp\">List Brand Config</a>"
			+ " &gt; <a href=\"canned-email-list.jsp?all=yes\">List Canned Email</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=\"canned-email-update.jsp?add=yes&brand_id=\" + dr_brand + \">Add Canned Email ...</a>";
	public String LinkPrintPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String StrHTML = "";
	public String advhtml = "";
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
	public String cannedemail_id = "0";
	public String course_name = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Canned SMS ID", "numeric", "cannedsms_id"},
			{"Brand Name", "text", "brand_name IN (SELECT brand_name FROM axela_brand WHERE brand_name"},
			{"Type", "text", "branchtype_name IN (SELECT branchtype_name FROM axela_branch_type WHERE branchtype_name"},
			{"Name", "text", "cannedemail_name"},
			{"Entry By", "text", "cannedemail_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "cannedemail_entry_date"},
			{"Modified By", "text", "cannedemail_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "cannedemail_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_email_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				msg = PadQuotes(request.getParameter("msg"));
				dr_brand = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				cannedemail_id = CNumeric(PadQuotes(request.getParameter("cannedemail_id")));
				all = PadQuotes(request.getParameter("all"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));

				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND cannedemail_id = 0";
				}
				else if (!(dr_brand.equals("0"))) {
					msg = msg + "<br>Results for Brand ID =" + dr_brand + "";
					StrSearch = StrSearch + " AND cannedemail_brand_id =" + dr_brand + "";
				} else if (!(cannedemail_id.equals("0"))) {
					msg = msg + "<br>Results for Canned Email Id =" + cannedemail_id + "";
					StrSearch = StrSearch + " AND cannedemail_id =" + cannedemail_id + "";
				}
				if (Up.equals("yes")) {
					MoveUp();
					response.sendRedirect(response.encodeRedirectURL("canned-email-list.jsp?dr_brand=" + dr_brand + "&msg=Email Moved UP Successfully!"));
				}
				if (Down.equals("yes")) {
					MoveDown();
					response.sendRedirect(response.encodeRedirectURL("canned-email-list.jsp?dr_brand=" + dr_brand + "&msg=Email Moved Down Successfully!"));
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
					} else {
						msg = "Result for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("cannedemailstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("cannedemailstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("cannedemailstrsql", StrSearch, request);
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
		StrSql = " SELECT cannedemail_id, cannedemail_name, brand_name, branchtype_name, cannedemail_brand_id,"
				+ " cannedemail_sub, cannedemail_active ";

		StrJoin = " FROM " + compdb(comp_id) + "axela_canned_email"
				+ " INNER JOIN axela_brand ON brand_id = cannedemail_brand_id"
				+ " INNER JOIN axela_branch_type ON branchtype_id = cannedemail_branchtype_id"
				+ " WHERE 1=1 ";

		CountSql = " SELECT COUNT(DISTINCT cannedemail_id) ";
		// if (!branch_id.equals("0")) {
		// // StrSearch = StrSearch + " and branch_id = " + branch_id + " ";
		// }
		StrSql = StrSql + StrJoin;
		CountSql = CountSql + StrJoin;
		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		// SOP("StrSql==" + StrSql);
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Canned Email(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "canned-email-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			if (all.equals("yes")) {
				StrSql = StrSql + "GROUP BY cannedemail_id  ORDER BY cannedemail_brand_id desc";
			} else {
				StrSql = StrSql + "  ORDER BY cannedemail_brand_id ,cannedemail_branchtype_id,cannedemail_rank";
			}
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
			try {
				// SOP("StrSql====" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = 0;
				// Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Brand</th>\n");
				Str.append("<th data-toggle=\"true\">Type</th>\n");
				Str.append("<th data-hide=\"phone\">Name</th>\n");
				Str.append("<th data-hide=\"phone\">Subject</th>\n");
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
					if (!crs.getString("cannedemail_active").equals("1")) {
						Str.append("<font color='red'>&nbsp;[Inactive]</font>");
					}

					Str.append("<td align=left>");
					if (!crs.getString("branchtype_name").equals("")) {
						Str.append(crs.getString("branchtype_name"));
					}
					Str.append("</td>");

					Str.append("</td>");
					Str.append("<td align=left>");
					if (!crs.getString("cannedemail_name").equals("")) {
						Str.append(crs.getString("cannedemail_name"));
					}
					Str.append("</td>");
					Str.append("<td align=left>");
					if (!crs.getString("cannedemail_sub").equals("")) {
						Str.append(crs.getString("cannedemail_sub"));
					}
					Str.append("</td>");
					Str.append("<td align=center><a href=\"canned-email-list.jsp?Up=yes&cannedemail_id=")
							.append(crs.getString("cannedemail_id")).append("&dr_brand=").append(crs.getString("cannedemail_brand_id"))
							.append(" \">Up</a> - <a href=\"canned-email-list.jsp?Down=yes&cannedemail_id=")
							.append(crs.getString("cannedemail_id")).append("&dr_brand=").append(crs.getString("cannedemail_brand_id"))
							.append(" \">Down</a></td>\n");
					Str.append("<td><a href=\"../portal/canned-email-update.jsp?update=yes&cannedemail_id=" + crs.getString("cannedemail_id"));
					Str.append(" \">Update Email</a></td>\n");

				}
				Str.append("</tbody></table>\n");
				// + "</div>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No Email(s) found!</font><br><br>";
		}
		return Str.toString();
	}
	public void MoveUp() {
		int tempRank;
		int cannedemail_rank;
		int cannedemail_brand_id;
		int cannedemail_branchtype_id;
		try {
			cannedemail_rank = Integer.parseInt(ExecuteQuery("SELECT cannedemail_rank "
					+ " FROM " + compdb(comp_id) + "axela_canned_email"
					+ " WHERE cannedemail_id=" + cannedemail_id + ""));

			cannedemail_brand_id = Integer.parseInt(ExecuteQuery("SELECT cannedemail_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_canned_email"
					+ " WHERE cannedemail_id = " + cannedemail_id));

			cannedemail_branchtype_id = Integer.parseInt(ExecuteQuery("SELECT cannedemail_branchtype_id "
					+ " FROM " + compdb(comp_id) + "axela_canned_email"
					+ " WHERE cannedemail_id = " + cannedemail_id));

			tempRank = Integer.parseInt(ExecuteQuery("select MIN(cannedemail_rank) as min1"
					+ " FROM " + compdb(comp_id) + "axela_canned_email"
					+ " WHERE cannedemail_brand_id = " + cannedemail_brand_id
					+ " AND cannedemail_branchtype_id = " + cannedemail_branchtype_id));

			if (cannedemail_rank != tempRank) {
				tempRank = cannedemail_rank - 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_canned_email"
						+ " SET cannedemail_rank = " + cannedemail_rank
						+ " WHERE cannedemail_rank = " + tempRank
						+ " AND cannedemail_brand_id=" + cannedemail_brand_id
						+ " AND cannedemail_branchtype_id=" + cannedemail_branchtype_id;

				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_canned_email "
						+ " SET cannedemail_rank=" + tempRank
						+ " WHERE cannedemail_rank = " + cannedemail_rank
						+ " AND cannedemail_id = " + cannedemail_id
						+ " AND cannedemail_brand_id = " + cannedemail_brand_id
						+ " AND cannedemail_branchtype_id=" + cannedemail_branchtype_id;
				updateQuery(StrSql);
			}
			dr_brand = Integer.toString(cannedemail_brand_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void MoveDown() {
		int tempRank;
		int cannedemail_rank;
		int cannedemail_brand_id;
		int cannedemail_branchtype_id;
		// SOP("sop==");
		try {
			cannedemail_rank = Integer.parseInt(ExecuteQuery("SELECT cannedemail_rank "
					+ " FROM " + compdb(comp_id) + "axela_canned_email"
					+ " WHERE cannedemail_id=" + cannedemail_id + ""));

			cannedemail_brand_id = Integer.parseInt(ExecuteQuery("SELECT cannedemail_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_canned_email"
					+ " WHERE cannedemail_id = " + cannedemail_id));

			cannedemail_branchtype_id = Integer.parseInt(ExecuteQuery("SELECT cannedemail_branchtype_id "
					+ " FROM " + compdb(comp_id) + "axela_canned_email"
					+ " WHERE cannedemail_id = " + cannedemail_id));

			tempRank = Integer.parseInt(ExecuteQuery("select MAX(cannedemail_rank) as max1"
					+ " FROM " + compdb(comp_id) + "axela_canned_email"
					+ " WHERE cannedemail_brand_id = " + cannedemail_brand_id
					+ " AND cannedemail_branchtype_id = " + cannedemail_branchtype_id));

			if (cannedemail_rank != tempRank) {
				tempRank = cannedemail_rank + 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_canned_email"
						+ " SET cannedemail_rank = " + cannedemail_rank
						+ " WHERE cannedemail_rank = " + tempRank
						+ " AND cannedemail_brand_id=" + cannedemail_brand_id
						+ " AND cannedemail_branchtype_id=" + cannedemail_branchtype_id;

				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_canned_email "
						+ " SET cannedemail_rank=" + tempRank
						+ " WHERE cannedemail_rank = " + cannedemail_rank
						+ " AND cannedemail_id = " + cannedemail_id
						+ " AND cannedemail_brand_id = " + cannedemail_brand_id
						+ " AND cannedemail_branchtype_id=" + cannedemail_branchtype_id;
				updateQuery(StrSql);
			}
			dr_brand = Integer.toString(cannedemail_brand_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
